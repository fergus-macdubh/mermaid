package com.vfasad.service;

import com.vfasad.entity.Option;
import com.vfasad.repo.OptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class OptionService {
    @Autowired
    OptionRepository optionRepository;

    public Collection<Option> findAll() {
        LinkedHashMap<OptionName, Option> optionMap = Arrays.stream(OptionName.values())
                .collect(LinkedHashMap::new,
                        (map, n) -> map.put(n, new Option(n)),
                        HashMap::putAll);

        optionRepository.findAll().forEach(option -> optionMap.put(option.getName(), option));

        return optionMap.values();
    }

    public void save(List<Option> options) {
        optionRepository.save(options);
    }
}
