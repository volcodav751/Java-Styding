using System;
using System.Collections.Generic;
using System.Text;

namespace Lb1
{
    internal class Address : IEntity
    {
        public int Id {  get; set; }
        public string StudentAddress {  get; set; }
        public int StudentId { get; set; }
        public Address(string studentAddress, int studentId) 
        { 
            StudentAddress= studentAddress;
            StudentId = studentId;
        }
        public override string ToString()
        {
            return $"{Id} {StudentAddress} {StudentId}";
        }
    }
}