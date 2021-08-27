import java.io.File;

/**
 * @author: JinSheng
 * @date: 2021/08/18 8:24 PM
 */
public class TestMakeDir {
    public static void main(String[] args) {
        File file = new File("/Users/chenjinsheng/Downloads/testupload/20210818/");
        boolean result = false;
        if (file.exists()){
            result = file.delete();
        }
        if (result){
            boolean mkdir = file.mkdir();
            System.out.println(mkdir);
        }
    }
}
