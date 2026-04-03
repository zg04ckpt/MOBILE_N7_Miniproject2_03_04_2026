package com.n7.miniproject2.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.n7.miniproject2.database.daos.CategoryDao;
import com.n7.miniproject2.database.daos.OrderDao;
import com.n7.miniproject2.database.daos.ProductDao;
import com.n7.miniproject2.database.daos.UserDao;
import com.n7.miniproject2.entities.Category;
import com.n7.miniproject2.entities.Order;
import com.n7.miniproject2.entities.OrderDetail;
import com.n7.miniproject2.entities.Product;
import com.n7.miniproject2.entities.User;

import java.util.concurrent.Executors;

@Database(entities = {User.class, Category.class, Product.class, Order.class, OrderDetail.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDb extends RoomDatabase {
    private static AppDb INSTANCE;

    public abstract UserDao userDao();
    public abstract ProductDao productDao();
    public abstract CategoryDao categoryDao();
    public abstract OrderDao orderDao();

    public static AppDb getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room
                    .databaseBuilder(context.getApplicationContext(), AppDb.class, "app_db")
                    .fallbackToDestructiveMigration()
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Executors.newSingleThreadExecutor().execute(() -> {
                                AppDb database = getInstance(context);

                                // Dữ liệu mẫu User
                                database.userDao().insert(new User(0, "admin", "admin123", "https://phunuvietnam.mediacdn.vn/179072216278405120/2023/2/2/z407811279454704d3b10be2c47b86e5bd170832bde562-16753562253021567389777.jpg"));
                                database.userDao().insert(new User(0, "user", "user123", "https://hthaostudio.com/wp-content/uploads/2020/07/Thanh-dat-xanh-4.jpg"));

                                // Dữ liệu mẫu Category
                                database.categoryDao().insert(new Category(1, "Hoa quả", "Các loại trái cây tươi ngon"));
                                database.categoryDao().insert(new Category(2, "Điện thoại", "Thiết bị di động thông minh"));
                                database.categoryDao().insert(new Category(3, "Ô tô", "Phương tiện di chuyển bốn bánh"));

                                // Dữ liệu mẫu Product - Hoa quả (categoryId = 1)
                                database.productDao().insert(new Product(0, "Quả cam tươi", "Cam sành mọng nước", 25000, "https://afamilycdn.com/150157425591193600/2023/12/21/sotsucojp-07252774-1703126712071-1703126712133223680555.jpg", "kg", 1));
                                database.productDao().insert(new Product(0, "Quả cam vàng", "Cam nhập khẩu", 45000, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTCvaNVj9vxQkNEdlj40r4fZuKxLmZ2amuLUA&s", "kg", 1));
                                database.productDao().insert(new Product(0, "Quả táo đỏ", "Táo Fuji ngọt giòn", 60000, "https://hoaquafuji.com/storage/app/media/uploaded-files/tao-4.jpg", "kg", 1));
                                database.productDao().insert(new Product(0, "Quả táo xanh", "Táo Mỹ nhập khẩu", 75000, "https://hoaquafuji.com/storage/app/media/uploaded-files/t%C3%A1o-2.jpg", "kg", 1));

                                // Dữ liệu mẫu Product - Điện thoại (categoryId = 2)
                                database.productDao().insert(new Product(0, "iPhone 17 Pro Max", "Cosmic Orange Edition", 35000000, "https://cdn2.fptshop.com.vn/unsafe/iphone_17_pro_max_cosmic_orange_1_a940b68476.png", "chiếc", 2));
                                database.productDao().insert(new Product(0, "iPhone 15 Plus", "Màu hồng cá tính", 22000000, "https://galaxydidong.vn/wp-content/uploads/2023/09/galaxy-di-dong-ip-15-plus-hong-01.jpg", "chiếc", 2));
                                database.productDao().insert(new Product(0, "Samsung Galaxy S25 Ultra", "Đỉnh cao công nghệ", 32000000, "https://www.didongmy.com/vnt_upload/product/01_2025/samsung_galaxy_s25_ultra_5g_blue_didongmy_thumb_600x600_1.jpg", "chiếc", 2));

                                // Dữ liệu mẫu Product - Ô tô (categoryId = 3)
                                database.productDao().insert(new Product(0, "VinFast VF7", "Xe điện hiện đại", 900000000, "https://vinfastvinhnghean.vn/domains/vinfastvinhnghean.vn/files/hinh_xe_VF7/6n9a7257.webp", "chiếc", 3));
                                database.productDao().insert(new Product(0, "VinFast VF3", "Xe điện mini năng động", 240000000, "https://vinfastotomiennam.com/uploads/images/2024/02/702x526-1708522199-multi_product10-img8771min.jpg", "chiếc", 3));
                                database.productDao().insert(new Product(0, "Ford Everest", "SUV mạnh mẽ", 1200000000, "https://fordphumy.com.vn/files/vn-sport-billboard-carousel-1-vZmN4WSg2d.jpg", "chiếc", 3));
                            });
                        }
                    })
                    .build();
        }
        return INSTANCE;
    }
}