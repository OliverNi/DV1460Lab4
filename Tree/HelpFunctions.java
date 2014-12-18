package Tree;

/**
 * Created by Oliver on 2014-12-18.
 */
public class HelpFunctions {
    private static HelpFunctions instance = new HelpFunctions();

    public static HelpFunctions getInstance() {
        return instance;
    }

    public static String[] removeLast(String[] arr){
        String[] newArr = new String[arr.length-1];

        for (int i = 0; i < arr.length-1; i++){
            newArr[i] = arr[i];
        }

        return newArr;
    }

    public static String[] addElement(String[] arr, String e){
        String[] newArr = new String[arr.length+1];
        for (int i = 0; i < arr.length; i++){
            newArr[i] = arr[i];
        }
        newArr[newArr.length-1] = e;

        return newArr;
    }
}
