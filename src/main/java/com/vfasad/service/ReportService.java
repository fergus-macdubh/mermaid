package com.vfasad.service;

import com.vfasad.entity.*;
import com.vfasad.entity.reports.ReportMonthCommon;
import com.vfasad.entity.reports.ReportMonthEmployee;
import com.vfasad.entity.reports.ReportMonthOption;
import com.vfasad.entity.reports.ReportMonthTeam;
import com.vfasad.repo.reports.ReportMonthCommonRepository;
import com.vfasad.repo.reports.ReportMonthEmployeeRepository;
import com.vfasad.repo.reports.ReportMonthOptionRepository;
import com.vfasad.repo.reports.ReportMonthTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Double.parseDouble;

@Service
public class ReportService {
    @Autowired
    ReportMonthOptionRepository reportMonthOptionRepository;
    @Autowired
    ReportMonthCommonRepository reportMonthCommonRepository;
    @Autowired
    ReportMonthTeamRepository reportMonthTeamRepository;
    @Autowired
    ReportMonthEmployeeRepository reportMonthEmployeeRepository;
    @Autowired
    OrderService orderService;
    @Autowired
    UserService userService;

    public void saveReport(Map<String, Option> options, int year, int month, User doneBy){
        saveReportMonthOptions(year, month, options);
        saveReportMonthTeam(year, month, options, doneBy);
        saveReportMonthEmployee(year, month, options, doneBy);
        saveReportMonthCommon(year, month, options, doneBy);
    }

    public Optional<ReportMonthCommon> findByYearAndMonthReportCommon(int year, int month) {
        return reportMonthCommonRepository.findByYearAndMonth(year, month);
    }

    public Map<String, String> findByYearAndMonthReportOptions(int year, int month) {
        return reportMonthOptionRepository.findByYearAndMonth(year, month).stream()
                .collect(Collectors.toMap((reportMonthOption) -> reportMonthOption.getName().toString(), ReportMonthOption::getValue));
    }

    public Collection<Option> findByYearAndMonthReportOptionList(int year, int month) {
        LinkedHashMap<OptionName, Option> optionMap = Arrays.stream(OptionName.values())
                .collect(LinkedHashMap::new,
                        (map, n) -> map.put(n, new Option(n)),
                        HashMap::putAll);

        reportMonthOptionRepository.findByYearAndMonth(year, month)
                .forEach(reportOption -> optionMap.put(reportOption.getName(), new Option(reportOption.getName(), reportOption.getValue())));
        return optionMap.values();
    }

    public List<ReportMonthTeam> findByYearAndMonthReportTeam(int year, int month) {
        return reportMonthTeamRepository.findByYearAndMonthOrderByTeamId(year, month);
    }

    public List<ReportMonthEmployee> findByYearAndMonthReportEmployee(int year, int month) {
        return reportMonthEmployeeRepository.findAllByYearAndMonthOrderByEmployeeId(year, month);
    }

    private void saveReportMonthOptions(int year, int month, Map<String, Option> options) {
        options.forEach((key, option) -> {
            ReportMonthOption reportMonthOption = new ReportMonthOption(year, month, option.getName());
            reportMonthOption.setValue(option.getValue());
            reportMonthOptionRepository.save(reportMonthOption);
        });
    }

    private void saveReportMonthCommon(int year, int month, Map<String, Option> options, User doneBy) {
        double sumArea = calculateSumArea(orderService.findByMonth(year, month), options);
        double electricity = parseDouble(options.get(OptionName.ELECTRICITY.toString()).getValue());
        double sumPriceConsumes = calculateSumPriceConsumes(orderService.findByMonth(year, month));
        double otherConsumes = calculateOtherConsumes(sumArea,
                parseDouble(options.get(OptionName.OTHER_CONSUMES.toString()).getValue()));
        double sumPrice = calculateSumPrice(orderService.findByMonth(year,month));
        double sumSalary = calculateSumSalary(options, year, month);
        double sumBonuses = calculateSumBonuses(year, month);
        double totalExpenses = calculateTotalExpenses(options, sumPriceConsumes, electricity, sumSalary, sumBonuses, otherConsumes);
        double totalIncome = sumPrice - totalExpenses;
        ReportMonthCommon reportMonthCommon = new ReportMonthCommon(year, month);
        reportMonthCommon.setSumArea(sumArea);
        reportMonthCommon.setElectricity(electricity);
        reportMonthCommon.setSumPriceConsumes(sumPriceConsumes);
        reportMonthCommon.setOtherConsumes(otherConsumes);
        reportMonthCommon.setSumPrice(sumPrice);
        reportMonthCommon.setTotalExpenses(totalExpenses);
        reportMonthCommon.setTotalIncome(totalIncome);
        reportMonthCommon.setSumSalary(sumSalary);
        reportMonthCommon.setSumBonuses(sumBonuses);
        reportMonthCommon.setOtherExpenses(parseDouble(options.get(OptionName.OTHER_EXPENSES.toString()).getValue()));
        reportMonthCommon.setBuildingRent(parseDouble(options.get(OptionName.BUILDING_RENT.toString()).getValue()));
        reportMonthCommon.setEnterprenuerTax(parseDouble(options.get(OptionName.ENTERPRENUER_TAX.toString()).getValue()));
        reportMonthCommon.setLineAmortization(parseDouble(options.get(OptionName.LINE_AMORTIZATION.toString()).getValue()));
        reportMonthCommon.setDoneBy(doneBy);

        reportMonthCommonRepository.save(reportMonthCommon);
    }

    private void saveReportMonthTeam(int year, int month, Map<String, Option> options, User doneBy) {
        List<Order> orders = orderService.findByMonth(year, month);
        Map<String, Team> teams = new HashMap<>();
        Map<Team, List<Order>> ordersByTeamId = orders.stream()
                .filter(o -> o.getTeam() != null)
                .peek(o -> teams.put(o.getTeam().getId().toString(), o.getTeam()))
                .collect(Collectors.groupingBy(Order::getTeam));
        Map<String, List<User>> usersByTeamId = userService.getUsersByTeamId();
        ordersByTeamId.forEach((team, ordersByTeam) -> {
            ReportMonthTeam reportMonthTeam = new ReportMonthTeam(year, month, team.getId());
            double areaByTeam = calculateSumArea(ordersByTeam, options);
            double salary = usersByTeamId
                    .getOrDefault(team.getId().toString(), new ArrayList<>())
                    .stream()
                    .mapToDouble((user) -> getUserSalary(user, options))
                    .sum();
            double bonus = usersByTeamId
                    .getOrDefault(team.getId().toString(), new ArrayList<>())
                    .stream()
                    .mapToDouble( (userByTeam) -> getUserBonus(userByTeam, options, areaByTeam))
                    .sum();
            reportMonthTeam.setTeamName(team.getName());
            reportMonthTeam.setArea(areaByTeam);
            reportMonthTeam.setIncome(calculateSumPrice(ordersByTeam));
            reportMonthTeam.setPaintExpenses(calculateSumPaintExpenses(ordersByTeam));
            reportMonthTeam.setPaintPrice(calculateSumPriceConsumes(ordersByTeam));
            reportMonthTeam.setSalary(salary);
            reportMonthTeam.setBonus(bonus);
            reportMonthTeam.setDoneBy(doneBy);
            reportMonthTeamRepository.save(reportMonthTeam);
        });
    }

    private void saveReportMonthEmployee(int year, int month, Map<String, Option> options, User doneBy) {
        List<Order> orders = orderService.findByMonth(year, month);
        Map<User, List<Order>> ordersByUserId = new HashMap<>();
        for (Order order : orders) {
            for (User user : order.getDoneBy()) {
                ordersByUserId.putIfAbsent(user, new ArrayList<>());
                ordersByUserId.get(user).add(order);
            }
        }
        ordersByUserId.forEach((user, ordersByUser) -> {
            ReportMonthEmployee reportMonthEmployee = new ReportMonthEmployee(year, month, user.getId());
            reportMonthEmployee.setEmployeeName(user.getName());
            double sumArea = calculateSumArea(ordersByUserId.get(user), options);
            reportMonthEmployee.setArea(sumArea);
            reportMonthEmployee.setSalary(getUserSalary(user, options));
            reportMonthEmployee.setBonus(getUserBonus(user, options, sumArea));
            reportMonthEmployee.setRole(user.getRole());
            reportMonthEmployee.setDoneBy(doneBy);
            reportMonthEmployeeRepository.save(reportMonthEmployee);
        });
    }

    private double calculateSumArea(List<Order> orders, Map<String, Option> options) {
        double clipToArea = parseDouble(options.get(OptionName.CLIP_TO_AREA.toString()).getValue());
        double furnitureSmallToArea = parseDouble(options.get(OptionName.FURNITURE_SMALL_TO_AREA.toString()).getValue());
        double furnitureBigToArea = parseDouble(options.get(OptionName.FURNITURE_BIG_TO_AREA.toString()).getValue());
        return orders.stream()
                .mapToDouble((o) -> o.getArea() +
                        o.getClipCount() * clipToArea +
                        o.getFurnitureSmallCount() * furnitureSmallToArea +
                        o.getFurnitureBigCount() * furnitureBigToArea)
                .sum();
    }

    private double calculateSumPriceConsumes(List<Order> orders) {
        return orders.stream()
                .mapToDouble((o) ->
                    o.getConsumes().stream().mapToDouble(
                            (c) -> c.getProduct().getPrice() * c.getActualUsedQuantity())
                            .sum())
                .sum();
    }

    private double calculateSumPaintExpenses(List<Order> orders) {
        return orders.stream()
                .mapToDouble((o) ->
                        o.getConsumes().stream().mapToDouble(OrderConsume::getActualUsedQuantity).sum())
                .sum();
    }

    private double calculateOtherConsumes(double sumArea, double otherConsumes) {
        return sumArea * otherConsumes;
    }

    private double calculateSumPrice(List<Order> orders) {
        return orders.stream().mapToDouble(Order::getPrice).sum();
    }

    private double calculateSumSalary(Map<String, Option> options, int year, int month) {
        return parseDouble(options.get(OptionName.SALARY_MANAGER.toString()).getValue())
                + reportMonthEmployeeRepository.findAllByYearAndMonthOrderByEmployeeId(year, month)
                    .stream()
                    .mapToDouble(ReportMonthEmployee::getSalary)
                    .sum();
    }

    private double calculateSumBonuses(int year, int month) {
        return reportMonthEmployeeRepository.findAllByYearAndMonthOrderByEmployeeId(year, month)
                .stream()
                .mapToDouble(ReportMonthEmployee::getBonus)
                .sum();
    }

    private double calculateTotalExpenses(Map<String, Option> options,
                                          double sumPriceConsumes,
                                          double electricity,
                                          double sumSalary,
                                          double sumBonuses,
                                          double otherConsumes) {
        return  sumPriceConsumes + electricity + otherConsumes + sumSalary + sumBonuses +
                parseDouble(options.get(OptionName.ENTERPRENUER_TAX.toString()).getValue()) +
                parseDouble(options.get(OptionName.ELECTRICITY.toString()).getValue()) +
                parseDouble(options.get(OptionName.BUILDING_RENT.toString()).getValue()) +
                parseDouble(options.get(OptionName.LINE_AMORTIZATION.toString()).getValue()) +
                parseDouble(options.get(OptionName.OTHER_EXPENSES.toString()).getValue());
    }

    private double getUserSalary(User user, Map<String, Option> options) {
        return user.getRole().equals("ROLE_PAINTER") ? parseDouble(options.get(OptionName.SALARY_PAINTER.toString()).getValue()) :
                user.getRole().equals("ROLE_LABORER") ? parseDouble(options.get(OptionName.SALARY_LABORER.toString()).getValue()) : 0;
    }

    private double getUserBonus(User user, Map<String, Option> options, double sumArea) {
        return sumArea *
                (user.getRole().equals("ROLE_PAINTER") ? parseDouble(options.get(OptionName.BONUS_PAINTER.toString()).getValue()) :
                        user.getRole().equals("ROLE_LABORER") ? parseDouble(options.get(OptionName.BONUS_LABORER.toString()).getValue()) : 0);
    }
}
