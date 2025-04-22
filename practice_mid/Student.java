public class Student {
    private String name;
    private double gpa;

    /** Constructors */
    public Student() {
        name = "Unknown";
        gpa = 0.0;
    }

    public Student(String n, double g) {
        name = n;
        setGPA(g); // 使用 setGPA 確保數值合法
    }

    /** Accessor methods */
    public String getName() {
        return name;
    }

    public double getGPA() {
        return gpa;
    }

    /** Mutator methods */
    public void setName(String n) {
        name = n;
    }

    public void setGPA(double g) {
        if ((g >= 0) && (g <= 4))
            gpa = g;
        else
            System.out.println("Invalid GPA value. Must be between 0 and 4.");
    }

    /** Facilitator methods */
    public String toString() { // 格式化輸出
        return name + " - GPA: " + gpa;
    }

    public boolean equals(Student s) { // 比較兩個學生是否相同（忽略大小寫）
        return this.name.equalsIgnoreCase(s.name);
    }

    public int compareTo(Student s) { // 用 GPA 來比較
        return Double.compare(this.gpa, s.gpa);
    }

    /** Main method for testing */
    public static void main(String[] args) {
        Student student1 = new Student("Alice", 3.8);
        Student student2 = new Student("Bob", 3.6);

        System.out.println(student1);
        System.out.println(student2);

        // 使用 equals() 方法比較學生是否相同
        if (student1.equals(student2)) {
            System.out.println("Alice and Bob are the same student.");
        } else {
            System.out.println("Alice and Bob are different students.");
        }

        // 使用 compareTo() 方法比較 GPA
        int comparison = student1.compareTo(student2);
        if (comparison > 0) {
            System.out.println("Alice has a higher GPA than Bob.");
        } else if (comparison < 0) {
            System.out.println("Bob has a higher GPA than Alice.");
        } else {
            System.out.println("Alice and Bob have the same GPA.");
        }
    }
}
