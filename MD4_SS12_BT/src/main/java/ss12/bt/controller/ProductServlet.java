package ss12.bt.controller;

import ss12.bt.model.Product;
import ss12.bt.service.ProductService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ProductServlet", value = "/products")
public class ProductServlet extends HttpServlet {
        protected ProductService productService = new ProductService();
        private List<Product> productList = productService.getAll();
        @Override
        public void init() throws ServletException {
                productService.save(new Product(1, "Car", 700000, "Black,S650", "Maybach"));
                productService.save(new Product(2, "Motorbike", 300000, "Green,Z1000", "Kawasaki"));
                productService.save(new Product(4, "Iphone 14 Pro Max", 1000, "Gold,128GB", "Apple"));
                productService.save(new Product(5, "Macbook Air", 2000, "M2,15inch", "Apple"));
                productService.save(new Product(6, "Watch", 3000, "Gold,RM88", "Richard Mille"));

        }
        protected void showListProducts(HttpServletRequest request,HttpServletResponse response) {
                request.setAttribute("products",productList);
                try {
                        request.getRequestDispatcher("WEB-INF/product/list.jsp").forward(request,response);
                } catch (ServletException | IOException e){
                        e.printStackTrace();
                }

        }
        protected void showCreateForm(HttpServletRequest request, HttpServletResponse response) {
                try {
                        request.getRequestDispatcher("WEB-INF/product/create.jsp").forward(request, response);
                } catch (ServletException | IOException e) {
                        e.printStackTrace();
                }
        }
        protected void showEditForm(HttpServletRequest request, HttpServletResponse response) {
                int id = Integer.parseInt(request.getParameter("id"));
                Product product = productService.findById(id);
                if(product == null){
                        request.getRequestDispatcher("error-404.jsp");
                } else {
                        request.setAttribute("product", product);
                        try {
                                request.getRequestDispatcher("WEB-INF/product/edit.jsp").forward(request, response);
                        } catch (ServletException | IOException e) {
                                e.printStackTrace();
                        }
                }
        }
        protected void showDeleteForm(HttpServletRequest request, HttpServletResponse response) {
                int id = Integer.parseInt(request.getParameter("id"));
                Product product = productService.findById(id);
                if(product == null){
                        request.getRequestDispatcher("error-404.jsp");
                } else {
                        request.setAttribute("product", product);
                        try {
                                request.getRequestDispatcher("WEB-INF/product/delete.jsp").forward(request, response);
                        } catch (ServletException | IOException e) {
                                e.printStackTrace();
                        }
                }

        }
        private void viewProduct(HttpServletRequest request, HttpServletResponse response) {
                int id = Integer.parseInt(request.getParameter("id"));
                Product product = productService.findById(id);
                request.setAttribute("product", product);
                try {
                        request.getRequestDispatcher("WEB-INF/product/view.jsp").forward(request, response);
                } catch (ServletException | IOException e) {
                        e.printStackTrace();
                }
        }
        private void searchProductByName(HttpServletRequest request, HttpServletResponse response){
                String keyword = request.getParameter("search");
                List<Product> searchList = new ArrayList<>();
                for (Product product : productList) {
                        if(product.getName().toLowerCase().contains(keyword.toLowerCase())){
                                searchList.add(product);
                        }
                }
                request.setAttribute("searchName",keyword);
                request.setAttribute("products",searchList);
                try {
                        request.getRequestDispatcher("WEB-INF/product/list.jsp").forward(request,response);
                } catch (ServletException | IOException e){
                        e.printStackTrace();
                }
        }
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                String action = request.getParameter("action");
                if (action == null) {
                        action = "";
                }
                switch (action) {
                        case "create":
                                showCreateForm(request, response);
                                break;
                        case "edit":
                                showEditForm(request, response);
                                break;
                        case "delete":
                                showDeleteForm(request, response);
                                break;
                        case "view":
                                viewProduct(request, response);
                                break;
                        case "Search":
                                searchProductByName(request, response);
                                break;
                        default:
                                showListProducts(request, response);
                                break;
                }
        }
        private void createProduct(HttpServletRequest request, HttpServletResponse response) {
                int id = productService.getNewId();
                String name = request.getParameter("name");
                double price = Double.parseDouble(request.getParameter("price"));
                String description = request.getParameter("description");
                String brand = request.getParameter("brand");
                Product product = new Product(id, name,price,description, brand);
                productService.save(product);
                request.setAttribute("message", "New product was created");
                try {
                        request.getRequestDispatcher("WEB-INF/product/create.jsp").forward(request, response);
                } catch (ServletException | IOException e) {
                        e.printStackTrace();
                }

        }
        protected void updateProduct(HttpServletRequest request, HttpServletResponse response) {
                int id = Integer.parseInt(request.getParameter("id"));
                String name = request.getParameter("name");
                double price = Double.parseDouble(request.getParameter("price"));
                String description = request.getParameter("description");
                String brand = request.getParameter("brand");
                Product product = productService.findById(id);
                if(product == null){
                        request.getRequestDispatcher("error-404.jsp");
                } else {
                        productList.set(productList.indexOf(product),new Product(id,name,price,description,brand));
                        request.setAttribute("product", product);
                        request.setAttribute("message", "Product information was updated");
                        try {
                                request.getRequestDispatcher("WEB-INF/product/edit.jsp").forward(request, response);
                        } catch (ServletException | IOException e) {
                                e.printStackTrace();
                        }
                }
        }
        protected void deleteProduct(HttpServletRequest request, HttpServletResponse response) {
                int id = Integer.parseInt(request.getParameter("id"));
                Product product = productService.findById(id);
                if(product == null){
                        request.getRequestDispatcher("error-404.jsp");
                } else {
                        productService.delete(id);
                        try {
                                response.sendRedirect("/products");
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
        }
        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                String action = request.getParameter("action");
                if (action == null) {
                        action = "";
                }
                switch (action) {
                        case "create":
                                createProduct(request, response);
                                break;
                        case "edit":
                                updateProduct(request, response);
                                break;
                        case "delete":
                                deleteProduct(request, response);
                                break;
                        default:
                                throw new RuntimeException("Invalid action");
                }
        }
}