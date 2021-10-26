package hanu.a2_1801040024.api;

import java.util.List;

import hanu.a2_1801040024.model.Products;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RestAPI {
    @GET("products")
    Call<List<Products>> getProducts();
}
