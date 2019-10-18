public class Example {
	public static void main(String[] args) {
		m1();
		m2();
	}
	public static void m1() {
		int a=1;
		if (a < 0)
			a++; else a--;
	}
	public static void m2() {
		m1();
		m1();
	}
}
