package com.cebedo.pmsys.service;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.cebedo.pmsys.domain.PullOut;

public interface PullOutService {

    public void rename(PullOut obj, String newKey);

    public void multiSet(Map<String, PullOut> m);

    public void set(PullOut obj);

    public void delete(Collection<String> keys);

    public void delete(String key);

    public void setIfAbsent(PullOut obj);

    public PullOut get(String key);

    public Set<String> keys(String pattern);

    public Collection<PullOut> multiGet(Collection<String> keys);

}
