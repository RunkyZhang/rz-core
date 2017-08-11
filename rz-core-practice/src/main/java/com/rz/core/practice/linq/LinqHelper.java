package com.rz.core.practice.linq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.rz.core.RZHelper;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.rz.core.practice.model.MonitorDto;

//Predicate<T>——接收T对象并返回boolean
//Consumer<T>——接收T对象，不返回值
//Function<T, R>——接收T对象，返回R对象
//Supplier<T>——提供T对象（例如工厂），不接收值
//UnaryOperator<T>——接收T对象，返回T对象
//BinaryOperator<T>——接收两个T对象，返回T对象，对于“reduce”操作很有用   
public class LinqHelper {
    public static void main(String[] args) {
        LinqHelper linqHelper = (LinqHelper) null;
        linqHelper = new LinqHelper();

        try {
            linqHelper.test();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("End LinqHelper...");
    }

    public void test() {
        // array to list
        String[] values1 = new String[10];
        List<String> values2 = Arrays.asList(values1);
        // array to list
        values1 = values2.toArray(new String[values2.size()]);
        values1 = values2.stream().toArray(o -> new String[o]);

        List<MonitorDto> monitorDtos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MonitorDto monitorDto = new MonitorDto();
            monitorDto.setAge(UUID.randomUUID().hashCode());
            monitorDto.setName("name" + String.valueOf(i));
            monitorDto.setPBool(0 == i % 2);
            monitorDtos.add(monitorDto);
        }

        // distinct
        //monitorDtos = monitorDtos.stream().filter(RZHelper.distinctByKey(o -> o.isPBool())).collect(Collectors.toList());
        // o -> o == Function.identity()
        // (k1, k2) -> k2) for when dup key, then use k1 or k2
        Map<Boolean, MonitorDto> map = monitorDtos.stream().collect(Collectors.toMap(MonitorDto::isPBool, o -> o, (k1, k2) -> k2));

        // mix: this = OrderBy(o => o.bool).ThenByDescending(o => o.int)
        List<MonitorDto> monitorDtosMix = monitorDtos.stream().sorted((o1, o2) -> Integer.compare(o1.getAge(), o2.getAge()))
                .sorted((o1, o2) -> Boolean.compare(o2.isPBool(), o1.isPBool())).collect(Collectors.toList());
        monitorDtosMix = monitorDtos.stream().sorted((o1, o2) -> Integer.compare(o1.getAge(), o2.getAge()))
                .collect(Collectors.groupingBy(o -> !((MonitorDto) o).isPBool(), Collectors.toList())).entrySet().stream().flatMap(o -> o.getValue().stream())
                .collect(Collectors.toList());
        for (MonitorDto item : monitorDtosMix) {
            System.out.println(item.isPBool() + " " + item.getAge());
        }

        // Where = filter, Select = map

        // filter
        monitorDtos = monitorDtos.stream().filter(o -> MonitorDto.class == o.getClass()).collect(Collectors.toList());

        // peek(do some thing in loop)
        monitorDtos = monitorDtos.stream().filter(o -> MonitorDto.class == o.getClass()).peek(o -> System.out.println("666" + o)).collect(Collectors.toList());

        // any
        System.out.println(monitorDtos.stream().findAny().isPresent());

        // first
        Optional<MonitorDto> optionalMonitorDto = monitorDtos.stream().filter(o -> 0 == o.getAge()).findFirst();
        if (true == optionalMonitorDto.isPresent()) {
            System.out.println("Not Null");
        } else {
            System.out.println("Null");
        }
        MonitorDto monitorDto = new MonitorDto();
        monitorDto.setAge(12222222);
        monitorDto.setName("12222222");
        // first -- if null, than use monitorDto
        System.out.println(optionalMonitorDto.orElse(monitorDto).toString());
        System.out.println(optionalMonitorDto.orElse(null));
        // first -- throw
        try {
            monitorDtos.stream().findFirst().orElseThrow(() -> {
                return new Exception("throw when no frist");
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // map (can Collectors.toSet)
        List<String> names = monitorDtos.stream().map(o -> o.getName()).collect(Collectors.toList());
        System.out.println(names.toString());

        // sort
        // int (o1, o2) asc, (o2, o1) desc
        // bool (o1, o2) false -> true, (o2, o1) true -> false
        System.out.println(monitorDtos.stream().sorted((o1, o2) -> Integer.compare(o1.getAge(), o2.getAge())).collect(Collectors.toList()));
        System.out.println(monitorDtos.stream().sorted((o1, o2) -> o2.getName().compareTo(o1.getName())).collect(Collectors.toList()));
        System.out.println(monitorDtos.stream().sorted((o1, o2) -> NumberUtils.compare(o1.getAge(), o2.getAge())).collect(Collectors.toList()));

        // max
        monitorDtos.stream().min((o1, o2) -> StringUtils.compare(o1.getName(), o2.getName())).get();

        // flatMap
        Stream<List<MonitorDto>> value10 = Stream.of(monitorDtos, monitorDtos);
        Stream<MonitorDto> value11 = value10.flatMap(o -> o.stream());

        // reduce
        // o1 previous object, o2 next object
        MonitorDto reduceMonitorDto = monitorDtos.stream().reduce((o1, o2) -> {
            MonitorDto temp = new MonitorDto();
            temp.setName(o1.getName() + o2.getName());
            return temp;
        }).get();

        // group by
        Map<Boolean, List<MonitorDto>> monitorDtosGroupBy = monitorDtos.stream().collect(Collectors.groupingBy(o -> ((MonitorDto) o).isPBool(), Collectors.toList()));
        for (Entry<Boolean, List<MonitorDto>> item : monitorDtosGroupBy.entrySet()) {
            System.out.println(item.getKey() + ": ");
            for (MonitorDto subItem : item.getValue()) {
                System.out.println(subItem.getAge());
            }
        }

        Map<Boolean, Long> monitorDtosGroupByCount = monitorDtos.stream().collect(Collectors.groupingBy(o -> ((MonitorDto) o).isPBool(), Collectors.counting()));
        monitorDtosGroupByCount = monitorDtos.stream().map(o -> o.pBool).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        for (Entry<Boolean, Long> item : monitorDtosGroupByCount.entrySet()) {
            System.out.println(item.getKey() + ": " + item.getValue());
        }

        // MonitorDto::getAge = o -> ((MonitorDto) o).getAge()
        Map<Boolean, Integer> monitorDtosGroupBySum = monitorDtos.stream()
                .collect(Collectors.groupingBy(o -> ((MonitorDto) o).isPBool(), Collectors.summingInt(MonitorDto::getAge)));
        for (Entry<Boolean, Integer> item : monitorDtosGroupBySum.entrySet()) {
            System.out.println(item.getKey() + ": " + item.getValue());
        }

        System.out.println(reduceMonitorDto);
    }

    public void issue() throws Exception {
        MonitorDto monitorDto = null;
        Date shardingDate = null;
        List<Date> dates = new ArrayList<Date>();
        for (Date date : dates) {
            List<MonitorDto> monitorDtos = null;
            try {
                monitorDtos = Arrays.asList(new MonitorDto(), new MonitorDto(), new MonitorDto());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            if (null != monitorDtos && true == monitorDtos.stream().findFirst().isPresent()) {
                // step 1
                monitorDto = monitorDtos.stream().sorted((o1, o2) -> NumberUtils.compare(o2.getAge(), o1.getAge())).findFirst().orElseThrow(null);
                shardingDate = date;
                break;
            }
        }
        if (null == monitorDto) {
            throw new Exception();
        }

        String name = monitorDto.getName();
        // same to [MonitorDto finalMonitorDto = monitorDto]
        final MonitorDto finalMonitorDto = monitorDto;
        List<String> appInfos = Arrays.asList("111", "222", "333");
        String appInfoName = null;

        // false: monitorDto is not final, cause set value to it at step 1
        // appInfoName = appInfos.stream().filter(o -> true ==
        // StringUtils.equals(monitorDto.getName(),
        // o)).findFirst().orElse(null);
        // true: finalMonitorDto is final
        appInfoName = appInfos.stream().filter(o -> true == StringUtils.equals(finalMonitorDto.getName(), o)).findFirst().orElse(null);
        // true: name is final
        appInfoName = appInfos.stream().filter(o -> true == StringUtils.equals(name, o)).findFirst().orElse(null);
        System.out.println(shardingDate.toString() + appInfoName + name);
    }

    public void issue1() {
        List<Integer> aaa = Arrays.asList(64004232, 33462968);
        List<Integer> bbb = Arrays.asList(64004232, 33462968);

        // wrong
        List<Integer> ccc = aaa.stream().peek(a -> {
            System.out.println(a + ": " + a.getClass().toString());
            System.out.println(bbb.get(0) + ": " + bbb.get(0).getClass().toString());
            System.out.println(a == bbb.get(0));
            System.out.println((int) a == bbb.get(0));
            System.out.println(a.equals(bbb.get(0)));
            System.out.println("---------------------------------------");
            System.out.println(a.hashCode());
            System.out.println(bbb.get(0).hashCode());
            System.out.println(a.hashCode() == bbb.get(0).hashCode());
            System.out.println("***************************************");
        }).filter(a -> bbb.stream().anyMatch(b -> a == b)).collect(Collectors.toList());
        System.out.println("Wrong-------" + ccc);

        // right
        for (int a : aaa) {
            if (bbb.stream().anyMatch(b -> a == b)) {
                ccc.add(a);
            }
        }
        System.out.println("Right-------" + ccc);
    }
}
