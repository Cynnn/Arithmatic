package evaluate;

import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

public class Evaluate {
	public static void main(String[] args) {
	Stack<Character> ops = new Stack<Character>();
	Stack<Fraction> vals = new Stack<Fraction>();
	Scanner input = new Scanner(System.in);
	int examNumber = Integer.parseInt(args[1]);
    int rightNumber = 0;
	System.out.println("���β��Թ�"+examNumber+"�⣬��"+examNumber*10+"��");
	for(int m=0;m<examNumber;m++) {
		String s = random();
		//String s = input.nextLine();
		System.out.println(s+"=");
		for(int i=0;i<s.length();i++) {
			char s1 = s.charAt(i);
		/*������ѹջ */
		if (s1 == '(') 
			ops.push(s1);
		/*�����Ű�֮ǰ�������������ջ��������*/
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
				////��ǰ����������ջ������������ȼ��ߣ���֮ǰ������������ֳ�ջ��������
				result = caculate(ops.pop(),a.getNumerator(),a.getDenominator(),b.getNumerator(),b.getDenominator()); 
				vals.push(new Fraction(result[0],result[1]));  //���������������ջ				
			}

			ops.push(s1);
		}
		else {
			if (s1 >= '0' && s1 <= '9') {
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
         //�ж��û�����Ľ���Ƿ���ȷ
         String userResult = input.nextLine();

         if(userResult.equals(rightResult)) {
        	 System.out.println("Right!");
        	 rightNumber++;        	 
         }
         else
        	 System.out.println("Wrong!");        
	}
	 System.out.println("��������"+rightNumber+"�⣬�÷�Ϊ"+rightNumber*10+"��");

	}
//�ж�����������ȼ�
public static boolean JudgePriority(char op1, char op2) {
	if (op2 == '(' || op2 == ')')
	    return false;
	if ((op1 == '*' || op1 == '��') && (op2 == '+' || op2 == '-'))
	    return false;
	else
	    return true;
    }
//
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

//����������ķ���
public static String random() {
	Random oprandom = new Random();
	Random fraction1 = new Random();
	Random fraction2 = new Random();
	//���������
	int opnumber = oprandom.nextInt(10)%10 +1;
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
	//��������ɵ������������������һ���ַ���
	String s1 = val[0];
	String s2 = "";
	for(int k=0;k<opnumber;k++) {
		s2 = s2+op[k]+val[k+1];		
	}
	String s = s1+s2;
    return s;
	
}


}
