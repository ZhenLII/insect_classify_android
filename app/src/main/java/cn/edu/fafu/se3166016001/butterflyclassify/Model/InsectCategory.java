package cn.edu.fafu.se3166016001.butterflyclassify.Model;

import java.lang.reflect.Array;
import java.util.List;

public class InsectCategory {
    private String categoryName;
    private List<Insect> Insects;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<Insect> getInsects() {
        return Insects;
    }

    public void setInsects(List<Insect> insects) {
        Insects = insects;
    }
}
