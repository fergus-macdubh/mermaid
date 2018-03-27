package com.vfasad.test.service;

import com.vfasad.entity.Option;
import com.vfasad.repo.OptionRepository;
import com.vfasad.service.OptionName;
import com.vfasad.service.OptionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RunWith(MockitoJUnitRunner.class)
public class OptionServiceTest {
    @Mock
    private OptionRepository optionRepository;

    @InjectMocks
    OptionService optionService;

    @Test
    public void testFindAll() {
        List<Option> optionList = generateOptionList(false);
        when(optionRepository.findAll()).thenReturn(new ArrayList<Option>(){{add(new Option());}});
        LinkedHashMap<OptionName, Option> expectedResult =
                optionList.stream().collect(Collectors.toMap(Option::getName, Function.identity(), (o1,o2) -> o2, LinkedHashMap::new));
        expectedResult.put(null, new Option());

        Collection<Option> resultOptionCollection = optionService.findAll();
        assertEquals("Invalid count in collection of Options", expectedResult.values().size(), resultOptionCollection.size());
        resultOptionCollection.forEach(option -> {
            assertTrue(String.format("Invalid option with the name '%s'", option.getName()), expectedResult.containsValue(option));
        });
    }

    @Test
    public void testSave() {
        List<Option> optionList = generateOptionList(true);
        optionService.save(optionList);
        verify(optionRepository, times(1)).save(optionList);
    }

    @Test
    public void testGetOptionsMap() {
        List<Option> optionList = generateOptionList(true);
        Map<String, String> expectedOptionMap =
                optionList.stream()
                        .collect(Collectors.toMap(Option::getValue, Option::getValue));
        optionList.add(new Option());
        when(optionRepository.findAll()).thenReturn(optionList);
        Map<String, String> resultOptionMap = optionService.getOptionsMap();
        assertEquals("Invalid Map of Options", expectedOptionMap, resultOptionMap);
    }

    private List<Option> generateOptionList(boolean withValues) {
        List<Option> optionList = new ArrayList<>();
        for (OptionName optionName: OptionName.values()){
            Option option = new Option(optionName);
            if (withValues) option.setValue(optionName.toString());
            optionList.add(option);
        }
        return optionList;
    }
}
