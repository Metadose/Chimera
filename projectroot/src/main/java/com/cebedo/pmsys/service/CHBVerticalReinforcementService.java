package com.cebedo.pmsys.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cebedo.pmsys.domain.CHBVerticalReinforcement;

public interface CHBVerticalReinforcementService {

    public void rename(CHBVerticalReinforcement obj, String newKey);

    public void multiSet(Map<String, CHBVerticalReinforcement> m);

    public String set(CHBVerticalReinforcement obj);

    public void delete(Collection<String> keys);

    public String delete(String key);

    public void setIfAbsent(CHBVerticalReinforcement obj);

    public CHBVerticalReinforcement get(String uuid);

    public Set<String> keys(String pattern);

    public Collection<CHBVerticalReinforcement> multiGet(Collection<String> keys);

    public List<CHBVerticalReinforcement> list();

}
