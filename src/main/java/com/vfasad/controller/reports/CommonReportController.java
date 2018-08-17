package com.vfasad.controller.reports;

import com.vfasad.entity.Option;
import com.vfasad.entity.Order;
import com.vfasad.entity.Team;
import com.vfasad.entity.User;
import com.vfasad.entity.reports.ReportMonthCommon;
import com.vfasad.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.vfasad.entity.User.ROLE_ADMIN;
import static java.lang.Double.parseDouble;
import static org.springframework.util.CollectionUtils.isEmpty;

@Controller
@Secured(ROLE_ADMIN)
public class CommonReportController extends AbstractReportController {
    @Autowired
    OrderService orderService;
    @Autowired
    OptionService optionService;
    @Autowired
    UserService userService;
    @Autowired
    TeamService teamService;
    @Autowired
    ReportService reportService;

    @GetMapping("/reports")
    public ModelAndView reports() {
        ModelAndView modelAndView = new ModelAndView("report/reports");
        return modelAndView;
    }

    @GetMapping("/reports/month")
    public ModelAndView allMonthesReport() {
        ModelAndView modelAndView = new ModelAndView("report/all-months-report");
        Map<String, List<Order>> ordersByMonth = orderService.findAll().stream()
                .filter(o -> o.getCompleted() != null)
                .collect(Collectors.groupingBy(o -> YearMonth.from(o.getCompleted()).toString()));
        SortedMap<String, List<Order>> ordersByMonthSorted = new TreeMap<>(ordersByMonth);
        Map<String, Boolean> monthReportIsOpened = ordersByMonthSorted.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        m -> !reportService.findByYearAndMonthReportCommon(
                                Year.from(m.getValue().get(0).getCompleted()).getValue(),
                                Month.from(m.getValue().get(0).getCompleted()).getValue()
                        ).isPresent()));
        SortedMap<String, Boolean> monthReportIsOpenedSorted = new TreeMap<>(monthReportIsOpened);
        modelAndView.addObject("ordersByMonth", ordersByMonthSorted);
        modelAndView.addObject("monthReportIsOpened", monthReportIsOpenedSorted);
        return modelAndView;
    }

    @GetMapping("/reports/month/{year}-{month}/team/{teamId}")
    public ModelAndView monthByTeamReport(@PathVariable int year,
                                          @PathVariable int month,
                                          @PathVariable long teamId) {
        ModelAndView modelAndView = new ModelAndView("report/month-by-team-report");
        modelAndView.addObject("month", getMonthName(month));
        modelAndView.addObject("year", year);
        modelAndView.addObject("team", teamService.getTeam(teamId));

        List<Order> orders = orderService.findByMonth(year, month).stream()
                .filter(order -> order.getTeam() != null)
                .filter(order -> order.getTeam().getId() == teamId)
                .collect(Collectors.toList());
        modelAndView.addObject("orders", orders);
        return modelAndView;
    }

    @GetMapping("/reports/month/{year}-{month}/user/{userId}")
    public ModelAndView monthByUserReport(@PathVariable int year,
                                          @PathVariable int month,
                                          @PathVariable long userId) {
        ModelAndView modelAndView = new ModelAndView("report/month-by-user-report");
        modelAndView.addObject("month", getMonthName(month));
        modelAndView.addObject("year", year);
        User user = userService.getUser(userId);
        modelAndView.addObject("user", user);

        List<Order> orders = orderService.findByMonth(year, month).stream()
                .filter(order -> !isEmpty(order.getDoneBy()))
                .filter(order -> order.getDoneBy().contains(user))
                .collect(Collectors.toList());
        modelAndView.addObject("orders", orders);
        return modelAndView;
    }

    @GetMapping("/reports/month/{year}-{month}")
    public ModelAndView monthReport(@PathVariable int year,
                                    @PathVariable int month) {
        ModelAndView modelAndView = new ModelAndView("report/month-report");
        modelAndView.addObject("month", getMonthName(month));
        modelAndView.addObject("monthNum", month);
        modelAndView.addObject("year", year);

        List<Order> orders = orderService.findByMonth(year, month);
        modelAndView.addObject("orders", orders);

        Map<String, Team> teams = new HashMap<>();
        Map<Long, List<Order>> ordersByTeamId = orders.stream()
                .filter(o -> o.getTeam() != null)
                .peek(o -> teams.put(o.getTeam().getId().toString(), o.getTeam()))
                .collect(Collectors.groupingBy(o -> o.getTeam().getId()));
        modelAndView.addObject("teams", teams);

        Map<String, List<Order>> ordersByTeamIdString = new HashMap<>();
        ordersByTeamId.keySet()
                .forEach(id -> ordersByTeamIdString.put(id.toString(), ordersByTeamId.get(id)));
        modelAndView.addObject("ordersByTeam", ordersByTeamIdString);

        Map<String, Double> areaByTeamId = new HashMap<>();
        ordersByTeamIdString.keySet()
                .forEach(teamId -> areaByTeamId.put(teamId, ordersByTeamIdString.get(teamId).stream().mapToDouble(this::getSumArea).sum()));
        modelAndView.addObject("areaByTeamId", areaByTeamId);

        modelAndView.addObject("usersByTeamId", userService.getUsersByTeamId());
        Map<String, String> options = reportService.findByYearAndMonthReportOptions(year, month);
        modelAndView.addObject("options", options.isEmpty() ? optionService.getOptionsMap() : options);

        Map<String, User> usersById = new HashMap<>();
        Map<String, List<Order>> ordersByUserId = new HashMap<>();
        for (Order order : orders) {
            for (User user : order.getDoneBy()) {
                if (user.isDeleted()) continue;
                List<Order> userOrders;
                if ((userOrders = ordersByUserId.get(user.getId().toString())) == null) {
                    userOrders = new ArrayList<>();
                    ordersByUserId.put(user.getId().toString(), userOrders);
                }
                userOrders.add(order);
                usersById.put(user.getId().toString(), user);
            }
        }
        modelAndView.addObject("ordersByUser", ordersByUserId);
        modelAndView.addObject("users", usersById);

        Map<String, Double> areaByUserId = new HashMap<>();
        ordersByUserId.keySet()
                .forEach(userId -> areaByUserId.put(userId, ordersByUserId.get(userId).stream().mapToDouble(this::getSumArea).sum()));
        modelAndView.addObject("areaByUserId", areaByUserId);

        Optional<ReportMonthCommon> closedReport = reportService.findByYearAndMonthReportCommon(year, month);
        modelAndView.addObject("reportIsOpened", !closedReport.isPresent());
        modelAndView.addObject("reportCommon", closedReport.orElse(null));
        modelAndView.addObject("reportTeamList", reportService.findByYearAndMonthReportTeam(year, month));
        modelAndView.addObject("reportEmployeeList", reportService.findByYearAndMonthReportEmployee(year, month));
        return modelAndView;
    }

    @GetMapping("/reports/month/{year}-{month}/options")
    @Secured(ROLE_ADMIN)
    public ModelAndView monthReportOptions(@PathVariable int year,
                                           @PathVariable int month) {
        ModelAndView modelAndView = new ModelAndView("report/month-report-options");
        modelAndView.addObject("month", getMonthName(month));
        modelAndView.addObject("monthNum", month);
        modelAndView.addObject("year", year);
        modelAndView.addObject("options", optionService.findAll());
        boolean reportIsOpened = (reportService.findByYearAndMonthReportCommon(year, month)).equals(Optional.empty());
        modelAndView.addObject("reportIsOpened", reportIsOpened);

        return modelAndView;
    }

    @PostMapping("/reports/month/{year}-{month}/options")
    @Secured(ROLE_ADMIN)
    public String monthReportOptions(@RequestParam String[] optionNames,
                                     @RequestParam String[] values,
                                     @PathVariable int year,
                                     @PathVariable int month) {
        Map<String, Option> options = IntStream.range(0, optionNames.length)
                .collect(HashMap::new,
                        (m, i) -> m.put(optionNames[i], new Option(OptionName.valueOf(optionNames[i]), values[i])),
                        Map::putAll);
        reportService.saveReport(options, year, month, userService.getCurrentUser());
        return "redirect:/reports/month/" + year + "-" + String.format("%02d", month);
    }

    private double getSumArea(Order order) {
        Map<String, String> options = optionService.getOptionsMap();

        return order.getArea() +
                order.getClipCount() * parseDouble(options.get(OptionName.CLIP_TO_AREA.name())) +
                order.getFurnitureSmallCount() * parseDouble(options.get(OptionName.FURNITURE_SMALL_TO_AREA.name())) +
                order.getFurnitureBigCount() * parseDouble(options.get(OptionName.FURNITURE_BIG_TO_AREA.name()));
    }
}
