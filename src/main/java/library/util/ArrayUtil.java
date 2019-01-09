package library.util;

public class ArrayUtil {

    public static int[] removeZeroes(int[] array) {
        int targetIndex = 0;

        for(int sourceIndex = 0; sourceIndex < array.length; sourceIndex++) {
            if(array[sourceIndex] != 0) {
                array[targetIndex++] = array[sourceIndex];
            }
        }

        int[] newArray = new int[targetIndex];
        System.arraycopy(array, 0, newArray, 0,targetIndex);

        return newArray;
    }

    public static float[] removeZeroes(float[] array) {
        int targetIndex = 0;

        for(int sourceIndex = 0; sourceIndex < array.length; sourceIndex++) {
            if(array[sourceIndex] != 0f) {
                array[targetIndex++] = array[sourceIndex];
            }
        }

        float[] newArray = new float[targetIndex];
        System.arraycopy(array, 0, newArray, 0,targetIndex);

        return newArray;
    }
}
