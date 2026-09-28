using System;
using System.Collections.Generic;
using System.Text;

namespace Lb1
{
    internal class DBItem<T> where T : IEntity
    {
        private int counter = 1;
        public List<T> Items = new List<T>();
        public int AddItem(T item)
        {
            item.Id = counter++;
            Items.Add(item);
            return item.Id;
        }
    }
}
