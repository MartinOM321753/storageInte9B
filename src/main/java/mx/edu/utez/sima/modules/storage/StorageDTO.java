package mx.edu.utez.sima.modules.storage;


import mx.edu.utez.sima.modules.category.Category;
import mx.edu.utez.sima.modules.user.BeanUser;


public class StorageDTO {

    private Long id;

    private String UUIDresponsible;

    private Long category;

    public Storage toEntity() {

        Storage storage = new Storage();
        storage.setStatus(true);
        BeanUser user = new BeanUser();
        user.setUuid(UUIDresponsible);
        storage.setResponsible(user);
        Category category = new Category();
        category.setId(this.category);
        storage.setCategory(category);
        return storage;
    }

    public Storage toEntityUpdate() {

        Storage storage = new Storage();
        storage.setId(this.id);
        storage.setStatus(true);
        BeanUser user = new BeanUser();
        user.setUuid(UUIDresponsible);
        storage.setResponsible(user);

        Category category = new Category();
        category.setId(this.category);
        storage.setCategory(category);

        return storage;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUUIDresponsible() {
        return UUIDresponsible;
    }

    public void setUUIDresponsible(String UUIDresponsible) {
        this.UUIDresponsible = UUIDresponsible;
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }
}
