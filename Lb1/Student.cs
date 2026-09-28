using System;
using System.Collections.Generic;
using System.Text;

namespace Lb1
{
    internal class Student : IEntity
    {
        public int Id { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public int Age { get; set; }

        public Student(string firstName, string lastName, int age)
        {
            FirstName = firstName;
            LastName = lastName;
            Age = age;
        }
        public override string ToString()
        {
            return $"{Id} {FirstName} {LastName} {Age}";
        }
    }
}