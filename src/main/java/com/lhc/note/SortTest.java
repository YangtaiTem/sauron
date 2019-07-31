package com.lhc.note;

import org.junit.Test;

import java.util.Arrays;

/**
 * @program: sauron
 * @description: 快排和归并排序
 * @author: hjt
 * @create: 2019-07-29 15:20
 **/

public class SortTest {
    @Test
    public void quickSortTset() {
        int [] a = {1,11,2,3,5,68,0,1};
        System.out.println("未排序的数组："+ Arrays.toString(a));
        if(a.length > 0){
            quickSort(a,0,a.length-1);
        }else{
            System.out.println("空数组不能排序");
        }
        System.out.println("排序后的数组："+Arrays.toString(a));
    }
    public static void quickSort(int[] a,int low,int high){
        //递归的出口
        if(low > high){
            return;
        }
        int i = low;
        int j = high;
        int key = a[low];
        while(i < j){
            //找到第一个比key小的数
            while(a[j] > key && i < j){
                j--;
            }
            //找到第一个比key大的数
            while(a[i] <= key && i < j){
                i++;
            }
            //如果i小于j，交换a[i],a[j]
            if(i < j){
                int temp = a[i];
                a[i] = a[j];
                a[j] = temp;
            }
            System.out.println("排序后的数组："+Arrays.toString(a));
        }
        int  p = a[i];
        a[i] = a[low];
        a[low] = p;//调整key的位置
        System.out.println("排序后的数组---："+Arrays.toString(a));
        quickSort(a,low,i-1);
        quickSort(a,i+1,high);
    }

    @Test
    public void mergeSortTest(){
        int [] a = {1,11,2,3,5,68,0,1};
        System.out.println("未排序的数组："+Arrays.toString(a));
        if(a.length > 0){
            mergeSort(a,0,a.length-1);
        }else{
            System.out.println("空数组不能排序");
        }
        System.out.println("排序后的数组："+Arrays.toString(a));
    }
    public static void mergeSort(int []a,int low,int high){
        int mid = (low + high) / 2;
        if(low < high){
            //左
            mergeSort(a,low,mid);
            //右
            mergeSort(a,mid+1,high);
            //左右合并
            merge(a,low,mid,high);
        }
    }
    public static void merge(int[]a,int low,int mid,int high){
        int [] mergeArr = new int[high-low+1];//申请一个新空间来保存排序后数组
        int i = low;
        int j = mid + 1;
        int k = 0;
        while(i <= mid && j <= high){
            if(a[i] < a[j]){
                mergeArr[k] = a[i];
                k++;
                i++;
            }else{
                mergeArr[k] = a[j];
                k++;
                j++;
            }
        }
        while(i <= mid){
            mergeArr[k] = a[i];
            k++;
            i++;
        }//把左边剩余的元素导入
        while(j <= high){
            mergeArr[k] = a[j];
            j++;
            k++;
        }//把右边剩余的元素导入
        for(int m = 0;m < mergeArr.length;m++){
            a[m+low] = mergeArr[m];
        }//将新排好序的数组放入元素相应的位置中
    }
}
