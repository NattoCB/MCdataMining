package com.snowk.mcdm.command.algorithm;

/* DBSCAN算法java实现，基于密度的聚类
 * 源码参考： outsider0007
 * Reference: https://blog.csdn.net/qq_37667364/article/details/89683499*/

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
 
public class DBSCAN {
	
    private static int ATRIBUTE_NUMBER = 3; // x y z
    //邻域半径
    private double epsilon = 0.3;
    //邻域内最小样本数量
    private int minimumNumberOfClusterMembers = 4;
    //数据集
    private ArrayList<double[]> inputValues = null;
    
    public static void main(CommandSender sender, ArrayList<double[]> arrayXYZ, List<Location> locationList, String task) {
        DBSCAN one = new DBSCAN();

        one.inputValues = arrayXYZ;
        boolean doNormalize = true;

		sender.sendMessage("§b开始执行§a§l DBSCAN §b聚类算法... §d(" + task + ")");

        int[] labels = one.performClustering(sender, doNormalize);
        printResult(sender, labels, locationList);
    }
    
    /**
     * 返回数据的标签不直接返回样本的划分
     */
    public int[] performClustering(CommandSender sender, boolean doNormalize) {
    	sender.sendMessage("§a样本总数: §f§l" + inputValues.size());
    	//是否数据归一化，对结果影响比较大
    	if(doNormalize)
    		dataNormalize();
    	int k = 0;//簇计数
    	List<Integer> unvisitedIndices = new ArrayList<>();
    	for(int i = 0; i < inputValues.size(); i++) {
    		unvisitedIndices.add(i);
    	}
    	//保存样本的类别信息 
    	// -1：噪音，0:未被访问，1,2,3,4...类别1，2，3，4...
    	int[] labels = new int[inputValues.size()];
    	int iter = 0;
    	while(unvisitedIndices.size() != 0) {
    		//0 随机选择一个没被访问过的样本作为开始
    		int index = (int) (Math.round(Math.random()*(unvisitedIndices.size()-1)));
    		index = unvisitedIndices.get(index);
    		//1获取该样本的邻域样本
    		List<Integer> indices = coreObjectTest(index);
    		//判断是否满足核心对象
    		if(indices.size() >= minimumNumberOfClusterMembers) {
    			//新生成簇进行扩展
    			k++;
    			//核心对象队列，也就是需要被扩展的核心对象
    			Map<Integer, List<Integer>> omega = new HashMap<>();
    			omega.put(index, indices);
    			//直到核心对象队列为空迭代停止
    			do {
    				//从核心对象列表中选择一个
    				int index_c = omega.entrySet().iterator().next().getKey();
    				labels[index_c] = k;
    				unvisitedIndices.remove(new Integer(index_c));
    				//获取邻域
    				List<Integer> neighborIndices = omega.get(index_c);
    				//从Omega中移除
    				omega.remove(index_c);
        			//遍历邻域
        			for(int index_ : neighborIndices) {
        				//未被访问或者之前是噪音
        				if(labels[index_] < 1) {
        					labels[index_] = k;
        					unvisitedIndices.remove(new Integer(index_));
        					//测试是否是核心对象,如果是加入到队列中
            				List<Integer> index_OfNeighborIndices = coreObjectTest(index_);
            				if(index_OfNeighborIndices.size() >= minimumNumberOfClusterMembers)
            					omega.put(index_, index_OfNeighborIndices);
        				}
        			}
    			}
    			while(omega.size() != 0);
    		} else {
    			//噪音
    			labels[index] = -1;
    			unvisitedIndices.remove(new Integer(index));
    		}
    		iter++;
    	}
    	sender.sendMessage("§e迭代次数:§f§l " + iter);
    	sender.sendMessage("=================================================");
		return labels;
    }
    
    public static void printResult(CommandSender sender, int[] labels, List<Location> locationList) {
    	// -1噪音，0,未被访问（不会出现），>=1类别
    	Map<Integer, ArrayList<Integer>> counts = new HashMap<>();
    	int c = 0;
    	for(int label : labels) {
    		ArrayList<Integer> indices = counts.get(label);
    		if(indices == null) {
    			ArrayList<Integer> list = new ArrayList<>();
    			list.add(c);
    			counts.put(label, list);
    		} else {
    			indices.add(c);
    		}
    		c++;
    	}
//    	sender.sendMessage("§e类 §a-1 §e为噪点类");
    	counts.forEach((k,v)->{
    		DecimalFormat dfInt = new DecimalFormat("#");
    		DecimalFormat df = new DecimalFormat("#.00");
    		String vsizeDF = df.format(v.size()*1.0/labels.length*100);
    		if (k != -1) {
        		double avgX = 0; double avgY = 0; double avgZ = 0;
            	String world = locationList.get(0).getWorld().getName();
            	sender.sendMessage("§b§l聚类结果: §e第§e§l"+ k +"§e类§a - 占总体比例: §c§l" + vsizeDF + "% §e个数统计: §f" + v.size() + "/" + labels.length);
            	for (int i : v) {              	// ArrayList<Integer> v // INDEX of LocationList
            		avgX += locationList.get(i).getX();
            		avgY += locationList.get(i).getY();
            		avgZ += locationList.get(i).getZ();
            	}
            	avgX /= v.size(); avgY /= v.size(); avgZ /= v.size();
            	sender.sendMessage("        - §c§l密集中心§b world-x-y-z: §a§l[§e§l" + world + "§a§l, " + dfInt.format(avgX) + ", " + dfInt.format(avgY) +", " + dfInt.format(avgZ) + "]");
        	} else {
        		sender.sendMessage("§b§l聚类结果: §e噪点类§a - 占总体比例: §c§l" + vsizeDF + "% §e个数统计: §f" + v.size() + "/" + labels.length);
        	}
    	});
    	sender.sendMessage("=================================================");
    }
    
    /**
     * 获取一个样本邻域的样本
     * @param testSampleIndex
     * @return 返回这些样本的index
     */
    public List<Integer> coreObjectTest(int testSampleIndex){
    	List<Integer> indices = new ArrayList<>();
    	for(int i = 0; i < inputValues.size(); i++) {
    		if(distance(inputValues.get(testSampleIndex), inputValues.get(i)) <= epsilon) {
    			indices.add(i);
    		}
    	}
    	return indices;
    }
    /**
     * 数据归一化
     * 如果不做归一化并且不修改weka中DBSCAN的设置那么结果将大不一样
     * x = (x - min)/(max - min)
     */
    public void dataNormalize() {
    	//x = (x - min)/(max - min)
    	double[] mins = new double[ATRIBUTE_NUMBER];
    	double[] maxs = new double[ATRIBUTE_NUMBER];
    	for(int i = 0; i < ATRIBUTE_NUMBER;i++) {
    		mins[i] = Double.MAX_VALUE;
    		maxs[i] = Double.MIN_VALUE;
    	}
    	for(int i = 0; i < ATRIBUTE_NUMBER; i++) {
    		for(int j = 0; j < inputValues.size();j++) {
    			mins[i] = inputValues.get(j)[i] < mins[i] ? inputValues.get(j)[i] : mins[i];
    			maxs[i] = inputValues.get(j)[i] > maxs[i] ? inputValues.get(j)[i] : maxs[i];
    		}
    	}
    	double[] maxsReduceMins = new double[ATRIBUTE_NUMBER];
    	for(int i = 0; i < ATRIBUTE_NUMBER;i++) {
    		maxsReduceMins[i] = maxs[i] - mins[i];
    	}
    	for(int i = 0; i < ATRIBUTE_NUMBER; i++) {
    		for(int j = 0; j < inputValues.size();j++) {
    			inputValues.get(j)[i] = (inputValues.get(j)[i] - mins[i])/(maxsReduceMins[i]);
    		}
    	}
    }
    /**
     * 欧式距离
     * @param v1
     * @param v2
     * @return
     */
    public double distance(double[] v1, double[] v2) {
		double sum = 0;
		for(int i = 0; i < v1.length; i++) {
			sum += Math.pow(v1[i]-v2[i], 2);
		}
		return Math.sqrt(sum);
	}
}