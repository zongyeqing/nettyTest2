package proxy;

/**
 * 请填写类注释
 *
 * @author 宗业清 
 * @since 2017年09月30日
 */
public class People {
    private String name;
    private String sex;

    public People(String name, String sex){
        this.name = name;
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
