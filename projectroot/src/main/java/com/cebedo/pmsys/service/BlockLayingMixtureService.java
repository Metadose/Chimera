package com.cebedo.pmsys.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cebedo.pmsys.domain.BlockLayingMixture;

public interface BlockLayingMixtureService {

    public void rename(BlockLayingMixture obj, String newKey);

    public void multiSet(Map<String, BlockLayingMixture> m);

    public String set(BlockLayingMixture obj);

    public void delete(Collection<String> keys);

    public String delete(String key);

    public void setIfAbsent(BlockLayingMixture obj);

    public BlockLayingMixture get(String uuid);

    public Set<String> keys(String pattern);

    public Collection<BlockLayingMixture> multiGet(Collection<String> keys);

    public List<BlockLayingMixture> list();

}
