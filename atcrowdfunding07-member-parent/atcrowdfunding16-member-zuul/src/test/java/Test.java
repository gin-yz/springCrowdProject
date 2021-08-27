/**
 * @author: JinSheng
 * @date: 2021/08/19 10:23 AM
 */
public class Test {
    public static String getCrowdProjectUrl(String rawUrl) {
        String crowdProjectUrl = "/project/**";
        int endIndex = crowdProjectUrl.indexOf("/", 1);
        String substring = crowdProjectUrl.substring(0, endIndex);
        boolean b = rawUrl.startsWith(substring);
        if (b) {
            return rawUrl.substring(endIndex);
        }
        return rawUrl;
    }

    public static void main(String[] args) {
        String crowdProjectUrl = getCrowdProjectUrl("/project/cjs");
        System.out.println(crowdProjectUrl);
    }
}
