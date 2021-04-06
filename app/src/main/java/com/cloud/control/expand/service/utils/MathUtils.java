package com.cloud.control.expand.service.utils;

import java.math.BigDecimal;

/**
 * @author wangyou
 * @desc: 数学计算相关工具类
 * @date :2021/3/10
 */
public class MathUtils {

    /**
     * 牛顿迭代法求平方根
     * @param x 需要开平方的数
     * @return 平方根
     */
    public static double sqrt(double x){
            if (x == 0) {
                return 0;
            }

            double C = x, x0 = x;
            while (true) {
                double xi = 0.5 * (x0 + C / x0);
                if (Math.abs(x0 - xi) < 1e-7) {
                    break;
                }
                x0 = xi;
            }
            return x0;
    }


        /**
         * @deprecated 开根号计算 大数速度太慢，不用
         * @param n  需要开根号的数据
         * @param m  需要保留的精度,即几位小数
         * @return
         */
        public static double MathSquare(double n, int m){
            double[] arr = new double[m];
            if(m >0){
                arr = sc(m);
            }
            int s = sq(n);

            return sb(n, s, arr);
        }

        /**
         * 计算整数位
         * @param n
         * @return
         */
        public static int sq(double n){
            if( n == 1){
                return 1;
            }
            int tmp = 0;
            for(int i=1;i<=n/2+1;i++){
                if(i*i == n){
                    tmp = i;
                    break;
                }
                if(i*i > n){
                    tmp = i-1;
                    break;
                }
            }
            return tmp;
        }

        /**
         * 计算要保留几位小数
         * @param m
         * @return
         */
        public static double[] sc(int m){
            double[] arr = new double[m];
            int num = 0;
            while(num != m){
                double f = 1;
                for(int i=0;i<=num;i++){
                    f = f*10;
                }
                arr[num] = 1/f;
                num++;
            }
            return arr;
        }

        /**
         * 开根号
         * @param n
         * @param j
         * @param arr
         * @return
         */
        public static double sb(double n, double j, double[] arr){
            double tmp = j;
            for(int p=0;p<arr.length;p++){
                if(p>0){
                    j = tmp;//计算过后的值（整数位+小数位的和，赋值给j，下面继续运算）
                }
                for(int i=1;i<=9;i++){//小数位只有九位{0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9}
                    tmp = i*arr[p]+j;//i*arr[p],相当于每次加0.1,0.2 ...
                    if(tmp*tmp == n){
                        return tmp;
                    }
                    if(tmp*tmp >n){
                        /*禁止使用构造方法BigDecimal(double)的方式把double值转化为BigDecimal对象 说明：反编译出的字节码文件显示每次循环都会new出一个StringBuilder对象，
                        然后进行append操作，最后通过toString方法返回String对象，造成内存资源浪费。
                        Negative example:
                        BigDecimal good1 = new BigDecimal(0.1);
                        Positive example:
                        BigDecimal good1 = new BigDecimal("0.1");
                        BigDecimal good2 = BigDecimal.valueOf(0.1);*/
                        //避免丢失精度
                        BigDecimal c1 = new BigDecimal(Double.toString(tmp));
                        BigDecimal c2 = new BigDecimal(Double.toString(arr[p]));
                        tmp = c1.subtract(c2).doubleValue();
                        break;
                    }
                }
            }
            return tmp;
        }
}