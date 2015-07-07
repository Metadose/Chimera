package com.cebedo.pmsys.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cebedo.pmsys.domain.CHBHorizontalReinforcement;

public interface CHBHorizontalReinforcementService {

    public void rename(CHBHorizontalReinforcement obj, String newKey);

    public void multiSet(Map<String, CHBHorizontalReinforcement> m);

    public String set(CHBHorizontalReinforcement obj);

    public void delete(Collection<String> keys);

    public String delete(String key);

    public void setIfAbsent(CHBHorizontalReinforcement obj);

    public CHBHorizontalReinforcement get(String uuid);

    public Set<String> keys(String pattern);

    public Collection<CHBHorizontalReinforcement> multiGet(
	    Collection<String> keys);

    public List<CHBHorizontalReinforcement> list();

}
