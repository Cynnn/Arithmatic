package evaluate;

import java.util.Random;
import java.util.Scanner;
import java.util.Stack;


public class Evaluate {  

	public static void main(String[] args) {	
	//int examNumber = 10; 
	int examNumber = Integer.parseInt(args[1]); 
	int rightNumber = 0;
	System.out.println("本次测试共"+examNumber+"题，共"+examNumber*10+"分");
	for(int m=0;m<examNumber;m++) {
		String s = random();
		while(s.charAt(0) == '(' && s.charAt(s.length()-1) ==')')
			s = random();	
		System.out.println("("+(m+1)+")"+" "+s+"=");		
         //判断用户输入的结果是否正确
         Scanner input = new Scanner(System.in);
         String userResult = input.nextLine();

         if(userResult.equals(evaluateAlgorithm(s))) {
        	 System.out.println("回答正确！");
        	 rightNumber++;        	 
         }
         else
        	 System.out.println("回答错误！正确结果为："+evaluateAlgorithm(s));        
	}
	 System.out.println("本次做对"+rightNumber+"题，得分为"+rightNumber*10+"，正确率为"+((double)rightNumber/examNumber)*100+"%");
	}
	
	
	//调度场算法，通过入栈出栈操作来计算结果
	public static String evaluateAlgorithm(String s) {
		Stack<Character> ops = new Stack<Character>();  //运算符栈
		Stack<Fraction> vals = new Stack<Fraction>();	//操作数栈
		for(int i=0;i<s.length();i++) {
			char s1 = s.charAt(i);
		//左括号入栈 
		if (s1 == '(') 
			ops.push(s1);
		//右括号把之前的数和运算符出栈进行运算
		else if(s1 == ')') {
			while(ops.peek()!= '(') {
				int result[] = new int[2];
				Fraction a = vals.pop();
				Fraction b = vals.pop();
				result = caculate(ops.pop(),a.getNumerator(),a.getDenominator(),b.getNumerator(),b.getDenominator());
				vals.push(new Fraction(result[0],result[1]));
			}

			ops.pop();
		}
		else if(s1 == '+' || s1 == '-' || s1 == '*' || s1 == '÷') {  //遇到运算符的情况
			while(!ops.empty() && JudgePriority(s1,ops.peek())) {    //判断运算符的优先级
				int result[] = new int[2];
				Fraction a = vals.pop();
				Fraction b = vals.pop();
				//当前运算符如果比栈顶运算符的优先级高，将之前的运算符和数字出栈进行运算
				result = caculate(ops.pop(),a.getNumerator(),a.getDenominator(),b.getNumerator(),b.getDenominator()); 
				vals.push(new Fraction(result[0],result[1]));  //将计算出的数字入栈				
			}

			ops.push(s1);
		}
		else {
			if (s1 >= '0' && s1 <= '9') {     //操作数入栈
				StringBuffer buf = new StringBuffer();
				while (i < s.length() && ((s.charAt(i) >='0' && s.charAt(i) <= '9') || s.charAt(i) == '/')) 
					buf.append(s.charAt(i++));								
			i--;
			String s2 = buf.toString();
			int flag = s2.length();
			for(int j=0;j<s2.length();j++) {  //寻找分号的位置
				if(s2.charAt(j) == '/') 
					flag = j;
				}
			StringBuffer buf1 = new StringBuffer();
			StringBuffer buf2 = new StringBuffer();
			for(int k=0;k<flag;k++) {        //分号之前的是分子
				buf1.append(s2.charAt(k));
			}
			if(flag != s2.length() ) {       //分号后面是分母
				for(int k=flag+1;k<s2.length();k++)
					buf2.append(s2.charAt(k));			
				
			}
			//整数的分母是1
			else buf2.append('1');
            //入栈
			vals.push(new Fraction(Integer.parseInt(buf1.toString()),Integer.parseInt(buf2.toString())));
			}
		} 
		}
		while(!ops.empty()) {
			int result[] = new int[2];
			Fraction a = vals.pop();
			Fraction b = vals.pop();
			result = caculate(ops.pop(),a.getNumerator(),a.getDenominator(),b.getNumerator(),b.getDenominator());
			vals.push(new Fraction(result[0],result[1]));
		}
		
         Fraction result = vals.pop();
         //最大公约数
         int k = GCD(result.numerator,result.denominator);
         //如果分母为1，只输出分子
         String rightResult;
         if(result.denominator/k == 1) {
        	 //System.out.println(result.numerator/k);
             rightResult = result.numerator/k + "";
         }
         else { //输出分数
        	 //System.out.println(result.numerator/k+"/"+result.denominator/k);
        	 rightResult = result.numerator/k+"/"+result.denominator/k + "";
         }
         return rightResult;
		
	}
	
    //判断运算符的优先级
	public static boolean JudgePriority(char op1, char op2) {
		if (op2 == '(' || op2 == ')')
			return false;
		if ((op1 == '*' || op1 == '÷') && (op2 == '+' || op2 == '-')) //op1比op2的优先级高
			return false;
		else
			return true;
	}
	
	//对不同运算符进行运算
	public  static int[] caculate(char op, int numerator1, int denominator1, int numerator2, int denominator2) { 
		int[] result = new int[2];
		switch (op) {
		case '+': 
			result[0] = numerator1*denominator2 + numerator2*denominator1; result[1]= denominator1*denominator2;
			return result;
		case '-':
			result[0] = numerator2*denominator1 - numerator1*denominator2; result[1]= denominator1*denominator2;
			return result;
		case '*':
			result[0] = numerator1*numerator2; result[1] = denominator1*denominator2;
			return result;
		case '÷':
			result[0] = numerator2*denominator1; result[1] = numerator1*denominator2;
			return result;
		}
		return result;
	}
	
	//求两个数的最大公约数
	public static int GCD(int a,int b) {
		//取绝对值，避免是负数
		a = Math.abs(a); 
		b = Math.abs(b);
		while(b!=0) {
			int temp = a%b;
			a = b;
			b = temp;
		}
		return a;
	}
	
	//产生随机运算式
	public static String random() {
		Random oprandom = new Random();
		Random fraction1 = new Random();
		Random fraction2 = new Random();
		Random bracket1 = new Random();
		Random bracket2 = new Random();
		//运算符个数
		int opnumber = oprandom.nextInt(5)%5+1;
		//操作数个数
		int valnumber = opnumber + 1;
		String[] op = new String[opnumber];
		String[] val = new String[valnumber];
		
		//生成运算符数组
		for (int i=0;i<opnumber;i++) {
			int oprange = oprandom.nextInt(4)%4 + 1;
			switch(oprange) {
			case 1: op[i]="+";break;
			case 2: op[i]="-";break;
			case 3: op[i]="*";break;
			case 4: op[i]="÷";
			} 
		}
		//生成操作数数组
		for (int j=0;j<valnumber;j++) {
			int b = (int)(Math.random()*10) % 2; 
			//整数
			if(b == 0)
				val[j] = (fraction1.nextInt(10)%10 +1) + "";
			//分数
			else {
				int num = fraction1.nextInt(10)%10 +1;
				int deno = fraction2.nextInt(10)%9 + 2;
				while(num >= deno) {
					num = fraction1.nextInt(10)%10 +1;
					deno = fraction2.nextInt(10)%9 + 2;
				}
				val[j] = num + "/" + deno + "";										
			}		
			
		}
		
		int m = (int)(Math.random()*10) % 2;
		if(m == 0) { //产生带括号的运算式
			int o = (int)(Math.random()*10) % 2;
			if (o == 0) { //先插入左括号，再插入右括号
				int[] lval1 = new int[valnumber]; //左括号标记数组
				int[] rval1 = new int[valnumber]; //右括号标记数组
				for (int k=0;k<valnumber-1;k++) {					
					int n = (int)(Math.random()*10) % 2;
					if(n == 0 && rval1[k] != 1) {
						lval1[k] = 1;           //标记为有左括号
						val[k] = "(" + val[k];  //操作数之前加上左括号
						int c = valnumber - 1;
						//找右括号的位置，必须在左括号的后面
						int d = bracket1.nextInt(c)%(c-k) + (k+1);
						//如果当前操作数之前有左括号，需要重新生成运算式
						while (lval1[d] == 1) 
							d = bracket1.nextInt(c)%(c-k) + (k+1);
						val[d] = val[d] +")";
						rval1[d] = 1;          //标记为有右括号
					} 
				}
			} 
				  
			else { //先插入右括号，再插入左括号
				int[] lval2 = new int[valnumber]; //左括号标记数组
				int[] rval2 = new int[valnumber]; //右括号标记数组
				//System.out.println("B");
				for (int k=valnumber-1;k>0;k--) {
					
					int n = (int)(Math.random()*10) % 2;
					if(n == 0 && lval2[k] != 1) {
						rval2[k] = 1;             //标记为有右括号
						val[k] = val[k] +")" ;    //操作数之后加上右括号
						int d = bracket2.nextInt(valnumber-1)%valnumber;
						//左括号的位置必须在右括号之前
						while (rval2[d] == 1 || d>=k)  
							d = bracket2.nextInt(valnumber-1)%valnumber;
						val[d] = "(" + val[d];
						lval2[d] = 1;
					}
				}
			}			 
		}
		
		//将随机生成的运算符和操作数连成一个字符串
		String s1 = val[0];
		String s2 = "";
		for(int k=0;k<opnumber;k++) {
			s2 = s2+op[k]+val[k+1];
		}
		String s = s1+s2;
		return s;
	}
	

}
