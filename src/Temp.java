
public class Temp
{
	public void m(){
		String s="";
		Object o=null;
		int i=0;
		int j=0;
		m1(s,o,i);
		int z=i+j;
		j=i++;
		j=++i;
	}
	
	public void m1(String s, Object o, int i){
		System.out.println(s);
		System.out.println(o);
		System.out.println(i);
	}

}
