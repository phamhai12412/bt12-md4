package ss12.bt.service;

import java.util.List;

public interface iGenericService<T,E> {
    List<T> getAll();
    T findById(E e);
    E getNewId();
    void save(T t);
    void delete(E e);

}
