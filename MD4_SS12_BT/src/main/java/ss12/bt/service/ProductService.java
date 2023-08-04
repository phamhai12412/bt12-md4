package ss12.bt.service;

import ss12.bt.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductService implements iGenericService<Product,Integer> {
    List<Product> productList;

    public ProductService() {
        productList = new ArrayList<>();
    }

    @Override
    public List<Product> getAll() {
        return productList;
    }

    @Override
    public Product findById(Integer id) {
        for (Product product : productList) {
            if (product.getId()==id){
                return product;
            }
        }
        return null;
    }

    @Override
    public Integer getNewId() {
       int maxId = 0;
        for (Product product : productList) {
            if(product.getId()>maxId){
                maxId = product.getId();
            }
        }
        return maxId + 1;
    }
    @Override
    public void save(Product product) {
        if(findById(product.getId())==null){
            productList.add(product);
        } else {
            productList.set(productList.indexOf(findById(product.getId())), product);
        }
    }

    @Override
    public void delete(Integer id) {
        productList.remove(findById(id));
    }
}
