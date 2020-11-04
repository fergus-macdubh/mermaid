package com.vfasad.test.service;

import com.vfasad.entity.*;
import com.vfasad.exception.NotFoundException;
import com.vfasad.repo.OrderRepository;
import com.vfasad.service.EmailService;
import com.vfasad.service.OrderService;
import com.vfasad.service.ProductService;
import com.vfasad.service.TeamService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {
    private static final Long ORDER_ID = 1L;
    private static final double AREA = 2.45;
    private static final int CLIP_COUNT = 5;
    private static final int FURNITURE_SMALL_COUNT = 8;
    private static final int FURNITURE_BIG_COUNT = 2;
    private static final String DOCUMENT = "document";
    private static final double PRICE = 4566.78;
    private static final Long USER_ID = 1L;
    private static final Long TEAM_ID = 1L;
    private static final long[] CONSUME_IDS = {1L, 2L, 3L};

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductService productService;

    @Mock
    private EmailService emailService;

    @Mock
    private TeamService teamService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @InjectMocks
    private OrderService orderService;

    @Test
    public void testFindAll() {
        List<Order> orderList = generateOrderList();
        when(orderRepository.findAll()).thenReturn(orderList);
        List<Order> resultOrderList = orderService.findAll();
        assertEquals("findAll returned incorrect order list", orderList, resultOrderList);
    }

    @Test
    public void testAddOrder() {
        orderService.addOrder(2.3, 5, 6, 7, "test", 798.87, Collections.emptySet(), LocalDate.now(), new Client());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    public void testGetOrder() {
        Order order = generateOrder();
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        Order resultOrder = orderService.getOrder(ORDER_ID);
        checkOrderDetails(order, resultOrder);
    }

    @Test
    public void testGetOrderNotFoundException() {
        expectedException.expect(NotFoundException.class);
        expectedException.expectMessage("Order with provided id is not found");
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());
        orderService.getOrder(ORDER_ID);
    }

    @Test
    public void testUpdateOrder(){
        Order order = generateOrder();
        Set<OrderConsume> orderConsumeSet = generateOrderConsumeList();

        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        orderService.updateOrder(
                ORDER_ID,
                AREA * 2,
                CLIP_COUNT * 2,
                FURNITURE_SMALL_COUNT * 2,
                FURNITURE_BIG_COUNT * 2,
                DOCUMENT + DOCUMENT,
                PRICE * 2,
                orderConsumeSet,
                LocalDate.now(),
                new Client(USER_ID * 2, "name", "phone", "contact", "email@email.com", new User(), false));
        checkOrderDetails(null, order);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void testGetActiveOrders() {
        List<Order> orderList = generateOrderList();
        List<BigInteger> blockedIds = new ArrayList<BigInteger>(){{add(BigInteger.valueOf(0)); add(BigInteger.valueOf(orderList.size()-1));}};

        when(orderRepository.findByStatusIn(OrderStatus.CREATED, OrderStatus.IN_PROGRESS, OrderStatus.SHIPPING)).thenReturn(orderList);
        when(orderRepository.findBlockedOrdersIds()).thenReturn(blockedIds);

        List<Order> resultOrderList = orderService.getActiveOrders();
        assertEquals("Order status had to be updated to BLOCKED", resultOrderList.get(0).getStatus(), OrderStatus.BLOCKED);
        assertEquals("Order status had to be updated to BLOCKED", resultOrderList.get(orderList.size()-1).getStatus(), OrderStatus.BLOCKED);
        resultOrderList.forEach(order -> checkOrderDetails(order, orderList.get(resultOrderList.indexOf(order))));
    }

    @Test
    public void testMoveOrderInProgress() {
        Order order = generateOrder();
        Set<OrderConsume> orderConsumes = generateOrderConsumeList();
        order.setConsumes(orderConsumes);
        Team team = generateTeam();
        List<User> userList = generateUserList();
        when(teamService.getTeam(TEAM_ID)).thenReturn(team);
        when(teamService.getTeamUsers(TEAM_ID)).thenReturn(userList);

        orderService.moveOrderInProgress(order, TEAM_ID, "/kanban");

        assertEquals("Invalid order status. Status should be IN_PROGRESS", OrderStatus.IN_PROGRESS, order.getStatus());
        assertEquals("Invalid order team", team, order.getTeam());
        assertEquals("Invalid user list", userList, order.getDoneBy());
        orderConsumes.forEach(c -> verify(productService, times(1)).spend(c.getProduct(), c.getCalculatedQuantity(), order));
        verify(orderRepository, times(1)).save(order);
        verify(emailService, times(1)).notifyManagerOrderInProgress(order, "/kanban");
    }

    @Test
    public void testMoveOrderToShipping() {
        Order order = generateOrder();
        Set<OrderConsume> orderConsumeList = generateOrderConsumeList();
        order.setConsumes(orderConsumeList);
        List<Double> actualQuantities = new ArrayList<Double>(){{ add(5262.33); add(28884.92); add(8395.39); }};

        orderService.moveOrderToShipping(order, CONSUME_IDS, actualQuantities, "/kanban");
        assertEquals("Invalid order status. Status should be SHIPPING", OrderStatus.SHIPPING, order.getStatus());
        verify(productService, times(CONSUME_IDS.length)).returnProduct(any(Product.class), anyDouble(), eq(order));
        verify(orderRepository, times(1)).save(order);
        verify(emailService, times(1)).notifyManagerOrderCompleted(order, "/kanban");
    }

    @Test
    public void testMoveOrderToShippingException() {
        expectedException.expect(NotFoundException.class);
        expectedException.expectMessage("Order consume with provided id is not found.");
        Order order = generateOrder();
        List<Double> actualQuantities = new ArrayList<Double>(){{ add(5262.33); add(28884.92); add(8395.39); }};
        orderService.moveOrderToShipping(order, CONSUME_IDS, actualQuantities,  "/kanban");
    }

    @Test
    public void testCloseOrder() {
        Order order = generateOrder();

        orderService.closeOrder(order);
        assertEquals("Invalid order status. Status should be CLOSED", OrderStatus.CLOSED, order.getStatus());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void testCancelOrderInProgress() {
        Order order = generateOrder();
        order.setStatus(OrderStatus.IN_PROGRESS);
        Set<OrderConsume> orderConsumes = generateOrderConsumeList();
        order.setConsumes(orderConsumes);
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        orderService.cancelOrder(ORDER_ID);
        assertEquals("Invalid order status. Status should be CREATED", OrderStatus.CREATED, order.getStatus());
        assertNull("Invalid order team. Team should be null", order.getTeam());
        verify(productService, times(orderConsumes.size())).returnProduct(any(Product.class), anyDouble(), eq(order));
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void testCancelOrderBlocked() {
        Order order = generateOrder();
        order.setStatus(OrderStatus.BLOCKED);
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        orderService.cancelOrder(ORDER_ID);
        assertEquals("Invalid order status. Status should be CANCELLED", OrderStatus.CANCELLED, order.getStatus());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void testCancelOrderShippingException() {
        Order order = generateOrder();
        order.setStatus(OrderStatus.SHIPPING);
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Unable to cancel order in status [" + order.getStatus() + "]");
        orderService.cancelOrder(ORDER_ID);
    }

    @Test
    public void testCancelOrderCancelledException() {
        Order order = generateOrder();
        order.setStatus(OrderStatus.CANCELLED);
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Unable to cancel order in status [" + order.getStatus() + "]");
        orderService.cancelOrder(ORDER_ID);
    }

    @Test
    public void testCancelOrderClosedException() {
        Order order = generateOrder();
        order.setStatus(OrderStatus.CLOSED);
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Unable to cancel order in status [" + order.getStatus() + "]");
        orderService.cancelOrder(ORDER_ID);
    }

    @Test
    public void testFindByMonth() {
        List<Order> orderList = generateOrderList();
        when(orderRepository.findByCompletedBetween(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(orderList);
        List<Order> resultOrderList = orderService.findByMonth(2018, 5);
        assertEquals("", orderList, resultOrderList);
    }

    private void checkOrderDetails(Order order, Order resultOrder) {
        assertEquals("Incorrect order client", order == null ? new Long(USER_ID*2) : order.getClient().getId(), resultOrder.getClient().getId());
        assertEquals("Incorrect order area", order == null ? AREA*2 : order.getArea(), resultOrder.getArea(), 0.00);
        assertEquals("Incorrect order clip count", order == null ? CLIP_COUNT*2 : order.getClipCount(), resultOrder.getClipCount());
        assertEquals("Incorrect order furniture small count", order == null ? FURNITURE_SMALL_COUNT*2 : order.getFurnitureSmallCount(), resultOrder.getFurnitureSmallCount());
        assertEquals("Incorrect order furniture big count", order == null? FURNITURE_BIG_COUNT*2 : order.getFurnitureBigCount(), resultOrder.getFurnitureBigCount());
        assertEquals("Incorrect order document", order == null ? DOCUMENT+DOCUMENT : order.getDocument(), resultOrder.getDocument());
        assertEquals("Incorrect order price", order == null ? PRICE*2 : order.getPrice(), resultOrder.getPrice(), 0.00);
        assertEquals("Incorrect order consumes", order == null ? generateOrderConsumeList() : order.getConsumes(), resultOrder.getConsumes());
        assertEquals("Incorrect order planned", order == null ? resultOrder.getPlanned() : order.getPlanned(), resultOrder.getPlanned());
    }

    private Order generateOrder() {
        Order order = new Order(AREA, CLIP_COUNT, FURNITURE_SMALL_COUNT, FURNITURE_BIG_COUNT, DOCUMENT, PRICE, Collections.emptySet(),LocalDate.now(), new Client());
        order.setId(ORDER_ID);
        return order;
    }

    private List<Order> generateOrderList() {
        List<Order> orderList = new ArrayList<>();
        Client client = generateClient();
        orderList.add(new Order(2.45, 5, 8, 2, "doc1", 4566.78, Collections.emptySet(),LocalDate.now(), client));
        orderList.add(new Order(40.87, 3, 23, 5, "doc5", 7281.33, Collections.emptySet(),LocalDate.now(), client));
        orderList.add(new Order(56.76, 6, 10, 4, "doc9", 9248.62, Collections.emptySet(),LocalDate.now(), client));
        orderList.add(new Order(10.99, 8, 1, 1, "doc8", 3374.72, Collections.emptySet(),LocalDate.now(), client));
        orderList.add(new Order(63.39, 12, 2, 2, "doc2", 7473.22, Collections.emptySet(),LocalDate.now(), client));
        orderList.add(new Order(38.56, 3, 3, 3, "doc3", 4263.94, Collections.emptySet(),LocalDate.now(), client));
        orderList.forEach(order -> {
            order.setStatus(orderList.indexOf(order)%3 == 0 ? OrderStatus.CREATED : OrderStatus.IN_PROGRESS);
            order.setId(Long.valueOf(orderList.indexOf(order)));
        });
        orderList.get(orderList.size()-1).setStatus(OrderStatus.SHIPPING);

        return orderList;
    }

    private User generateUser(Long id) {
        User user = new User("email","name","givenName","familyName",null,"gender","en");
        user.setId(id);
        return user;
    }

    private Client generateClient() {
        User manager = generateUser(USER_ID);
        return new Client(1L, "client name", "client phone", "client contact", "client email", manager, false);
    }

    private List<User> generateUserList() {
        List<User> userList = new ArrayList<>();
        userList.add(new User("test@gmail.com","Name","GivenName","FamilyName",null,"female","en"));
        userList.add(new User("test2@gmail.com","Name2","GivenName2","FamilyName2",null,"female","en"));

        return userList;
    }

    private Team generateTeam() {
        Team team = new Team();
        team.setName("name");
        team.setColor("color");
        team.setId(TEAM_ID);

        return team;
    }

    private Product generateProduct(long id, String name, Product.Unit unit, String producer, String supplier) {
        Product product = new Product(name, unit, producer, supplier);
        product.setId(id);
        return product;
    }

    private OrderConsume generateOrderConsume(long id, Product product, double calculatedQuantity) {
        OrderConsume orderConsume = new OrderConsume(product,calculatedQuantity);
        orderConsume.setId(id);
        return orderConsume;
    }

    private Set<OrderConsume> generateOrderConsumeList() {
        HashSet<OrderConsume> orderConsumes = new HashSet<>();
        for(long id: CONSUME_IDS) {
            orderConsumes.add(generateOrderConsume(
                    id
                    ,generateProduct(id,"name"+id, Product.Unit.LITER,"producer"+id,"supplier"+id)
                    ,9292.38*id));
        }
        return orderConsumes;
    }
}
