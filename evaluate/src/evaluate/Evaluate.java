package evaluate;

import java.util.Random;
import java.util.Scanner;
import java.util.Stack;


public class Evaluate {  

	public static void main(String[] args) {	
	//int examNumber = 10; 
	int examNumber = Integer.parseInt(args[1]); 
	int rightNumber = 0;
	System.out.println("���β��Թ�"+examNumber+"�⣬��"+examNumber*10+"��");
	for(int m=0;m<examNumber;m++) {
		String s = random();
		while(s.charAt(0) == '(' && s.charAt(s.length()-1) ==')')
			s = random();	
		System.out.println("("+(m+1)+")"+" "+s+"=");		
         //�ж��û�����Ľ���Ƿ���ȷ
         Scanner input = new Scanner(System.in);
         String userResult = input.nextLine();

         if(userResult.equals(evaluateAlgorithm(s))) {
        	 System.out.println("�ش���ȷ��");
        	 rightNumber++;        	 
         }
         else
        	 System.out.println("�ش������ȷ���Ϊ��"+evaluateAlgorithm(s));        
	}
	 System.out.println("��������"+rightNumber+"�⣬�÷�Ϊ"+rightNumber*10+"����ȷ��Ϊ"+((double)rightNumber/examNumber)*100+"%");
	}
	
	
	//���ȳ��㷨��ͨ����ջ��ջ������������
	public static String evaluateAlgorithm(String s) {
		Stack<Character> ops = new Stack<Character>();  //�����ջ
		Stack<Fraction> vals = new Stack<Fraction>();	//������ջ
		for(int i=0;i<s.length();i++) {
			char s1 = s.charAt(i);
		//��������ջ 
		if (s1 == '(') 
			ops.push(s1);
		//�����Ű�֮ǰ�������������ջ��������
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
		else if(s1 == '+' || s1 == '-' || s1 == '*' || s1 == '��') {  //��������������
			while(!ops.empty() && JudgePriority(s1,ops.peek())) {    //�ж�����������ȼ�
				int result[] = new int[2];
				Fraction a = vals.pop();
				Fraction b = vals.pop();
				//��ǰ����������ջ������������ȼ��ߣ���֮ǰ������������ֳ�ջ��������
				result = caculate(ops.pop(),a.getNumerator(),a.getDenominator(),b.getNumerator(),b.getDenominator()); 
				vals.push(new Fraction(result[0],result[1]));  //���������������ջ				
			}

			ops.push(s1);
		}
		else {
			if (s1 >= '0' && s1 <= '9') {     //��������ջ
				StringBuffer buf = new StringBuffer();
				while (i < s.length() && ((s.charAt(i) >='0' && s.charAt(i) <= '9') || s.charAt(i) == '/')) 
					buf.append(s.charAt(i++));								
			i--;
			String s2 = buf.toString();
			int flag = s2.length();
			for(int j=0;j<s2.length();j++) {  //Ѱ�ҷֺŵ�λ��
				if(s2.charAt(j) == '/') 
					flag = j;
				}
			StringBuffer buf1 = new StringBuffer();
			StringBuffer buf2 = new StringBuffer();
			for(int k=0;k<flag;k++) {        //�ֺ�֮ǰ���Ƿ���
				buf1.append(s2.charAt(k));
			}
			if(flag != s2.length() ) {       //�ֺź����Ƿ�ĸ
				for(int k=flag+1;k<s2.length();k++)
					buf2.append(s2.charAt(k));			
				
			}
			//�����ķ�ĸ��1
			else buf2.append('1');
            //��ջ
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
         //���Լ��
         int k = GCD(result.numerator,result.denominator);
         //�����ĸΪ1��ֻ�������
         String rightResult;
         if(result.denominator/k == 1) {
        	 //System.out.println(result.numerator/k);
             rightResult = result.numerator/k + "";
         }
         else { //�������
        	 //System.out.println(result.numerator/k+"/"+result.denominator/k);
        	 rightResult = result.numerator/k+"/"+result.denominator/k + "";
         }
         return rightResult;
		
	}
	
    //�ж�����������ȼ�
	public static boolean JudgePriority(char op1, char op2) {
		if (op2 == '(' || op2 == ')')
			return false;
		if ((op1 == '*' || op1 == '��') && (op2 == '+' || op2 == '-')) //op1��op2�����ȼ���
			return false;
		else
			return true;
	}
	
	//�Բ�ͬ�������������
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
		case '��':
			result[0] = numerator2*denominator1; result[1] = numerator1*denominator2;
			return result;
		}
		return result;
	}
	
	//�������������Լ��
	public static int GCD(int a,int b) {
		//ȡ����ֵ�������Ǹ���
		a = Math.abs(a); 
		b = Math.abs(b);
		while(b!=0) {
			int temp = a%b;
			a = b;
			b = temp;
		}
		return a;
	}
	
	//�����������ʽ
	public static String random() {
		Random oprandom = new Random();
		Random fraction1 = new Random();
		Random fraction2 = new Random();
		Random bracket1 = new Random();
		Random bracket2 = new Random();
		//���������
		int opnumber = oprandom.nextInt(5)%5+1;
		//����������
		int valnumber = opnumber + 1;
		String[] op = new String[opnumber];
		String[] val = new String[valnumber];
		
		//�������������
		for (int i=0;i<opnumber;i++) {
			int oprange = oprandom.nextInt(4)%4 + 1;
			switch(oprange) {
			case 1: op[i]="+";break;
			case 2: op[i]="-";break;
			case 3: op[i]="*";break;
			case 4: op[i]="��";
			} 
		}
		//���ɲ���������
		for (int j=0;j<valnumber;j++) {
			int b = (int)(Math.random()*10) % 2; 
			//����
			if(b == 0)
				val[j] = (fraction1.nextInt(10)%10 +1) + "";
			//����
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
		if(m == 0) { //���������ŵ�����ʽ
			int o = (int)(Math.random()*10) % 2;
			if (o == 0) { //�Ȳ��������ţ��ٲ���������
				int[] lval1 = new int[valnumber]; //�����ű������
				int[] rval1 = new int[valnumber]; //�����ű������
				for (int k=0;k<valnumber-1;k++) {					
					int n = (int)(Math.random()*10) % 2;
					if(n == 0 && rval1[k] != 1) {
						lval1[k] = 1;           //���Ϊ��������
						val[k] = "(" + val[k];  //������֮ǰ����������
						int c = valnumber - 1;
						//�������ŵ�λ�ã������������ŵĺ���
						int d = bracket1.nextInt(c)%(c-k) + (k+1);
						//�����ǰ������֮ǰ�������ţ���Ҫ������������ʽ
						while (lval1[d] == 1) 
							d = bracket1.nextInt(c)%(c-k) + (k+1);
						val[d] = val[d] +")";
						rval1[d] = 1;          //���Ϊ��������
					} 
				}
			} 
				  
			else { //�Ȳ��������ţ��ٲ���������
				int[] lval2 = new int[valnumber]; //�����ű������
				int[] rval2 = new int[valnumber]; //�����ű������
				//System.out.println("B");
				for (int k=valnumber-1;k>0;k--) {
					
					int n = (int)(Math.random()*10) % 2;
					if(n == 0 && lval2[k] != 1) {
						rval2[k] = 1;             //���Ϊ��������
						val[k] = val[k] +")" ;    //������֮�����������
						int d = bracket2.nextInt(valnumber-1)%valnumber;
						//�����ŵ�λ�ñ�����������֮ǰ
						while (rval2[d] == 1 || d>=k)  
							d = bracket2.nextInt(valnumber-1)%valnumber;
						val[d] = "(" + val[d];
						lval2[d] = 1;
					}
				}
			}			 
		}
		
		//��������ɵ�������Ͳ���������һ���ַ���
		String s1 = val[0];
		String s2 = "";
		for(int k=0;k<opnumber;k++) {
			s2 = s2+op[k]+val[k+1];
		}
		String s = s1+s2;
		return s;
	}
	

}
