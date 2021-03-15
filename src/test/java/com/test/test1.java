package com.test;

import java.util.Arrays;

public class test1 {
    public static void main(String[] args) {
        int arr[] = {-9, 78, 0, 23, -567, 70};
        QuickSort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));
    }

    public static void selectSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int minIndex = i;
            int min = arr[i];
            for (int j = i + 1; j < arr.length; j++) {
                if (min > arr[j]) {
                    min = arr[j];
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                arr[minIndex] = arr[i];
                arr[i] = min;
            }
            System.out.println("第" + (i + 1) + "轮后");
            System.out.println(Arrays.toString(arr));
        }

    }

    public static void BubbeltSort(int[] arr) {
        int temp = 0;
        boolean flag = false;
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    flag = true;
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
            System.out.println("第" + (i + 1) + "趟" + Arrays.toString(arr));
            if (!flag)
                break;
            else
                flag = false;
        }
        System.out.println(Arrays.toString(arr));
    }

    public static void QuickSort(int[] arr, int left, int right) {
        int l = left; //左下标
        int r = right;//右下标
        int temp = 0;
        int mid = arr[(left + right) / 2];
        //循环的目的：让比mid小的放到它的左边，比mid大的放到右边
        while (l < r) {
            //在mid的左边一直找，找到比mid大或者相等的值
            while (arr[l] < mid) {
                l += 1;
            }
            //在mid的右边一直找，找到比mid小或者相等的值
            while (arr[r] > mid) {
                r -= 1;
            }
            //如果l>=r 说明mid的左右的值，已经按照左边全是小于mid的值，右边全是大于等于mid的值
            if (l >= r)
                break;
            //交换
            temp = arr[l];
            arr[l] = arr[r];
            arr[r] = temp;
            //交换完后 发现 arr[l]==mid
            if (arr[l] == mid)
                r -= 1;
            if (arr[r] == mid)
                l += 1;
        }
        if (l == r) {
            l += 1;
            r -= 1;
        }
        //向左递归
        if (left < r)
            QuickSort(arr, left, r);
        //向右递归
        if (right > l)
            QuickSort(arr, right, l);
    }
}
