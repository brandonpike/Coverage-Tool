public class ExampleTransformed {
	public static void main(String[] args) {
		System.out.println("main() executed");
		m1();
		m2();
	}

	public static void m1() {
		System.out.println("m1() executed");
		int a=1;
		if (a < 0)
			a++; else a--;
	}

	public static void m2() {
		System.out.println("m2() executed");
		m1();
		m1();
	}
}
