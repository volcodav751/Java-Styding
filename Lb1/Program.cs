//2. Варіант. Створити реляційні таблиці з моделюванням відношень один
//до - одного, один - до - багатьох, багато - до - багатьох системи автоматизації роботи
//університету.
namespace Lb1
{
    internal class Program
    {
        static void Main(string[] args)
        {
            Student student1 = new Student("Michael", "Jackson", 20);
            Student student2 = new Student("Ryan", "Gosling", 19);
            Student student3 = new Student("Arnold", "Schwarzenegger", 21);

            Address address1 = new Address("New York", 1);
            Address address2 = new Address("Thal", 3);
            Address address3 = new Address("London", 2);

            DBItem<Student> DBStudent = new DBItem<Student>();
            DBStudent.AddItem(student1);
            DBStudent.AddItem(student2);
            DBStudent.AddItem(student3);

            DBItem<Address> DBAddress = new DBItem<Address>();
            DBAddress.AddItem(address1);
            DBAddress.AddItem(address2);
            DBAddress.AddItem(address3);

            foreach (Student s in DBStudent.Items)
            {
                Console.WriteLine(s);
                foreach(Address a in DBAddress.Items)
                {
                    if (s.Id == a.StudentId)
                    {
                        Console.WriteLine("\t" + a);
                    }
                }
            }
        }
    }
}
