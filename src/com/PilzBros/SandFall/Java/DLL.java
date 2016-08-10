package com.PilzBros.SandFall.Java;



//****************************  DLL.java  *******************************
//generic doubly linked list class

public class DLL<T> {
private DLLNode<T> head, tail;
	public DLL() {
	head = tail = null;
	}
	public boolean isEmpty() {
	return head == null;
	}
	public void clear() {
	head = tail = null;
	}
	public T firstEl() {
	if (head != null)
	return head.info;
	else return null;
	}
	public void addToHead(T el) {
	if (head != null) {
	head = new DLLNode<T>(el,head,null);
	head.next.prev = head;
	}
	else head = tail = new DLLNode<T>(el);
	}
	public void addToTail(T el) {
	if (tail != null) {
	tail = new DLLNode<T>(el,null,tail);
	tail.prev.next = tail;
	}
	else head = tail = new DLLNode<T>(el);
	}
	public T deleteFromHead() {
	if (isEmpty()) 
	return null;
	T el = head.info;
	if (head == tail)   // if only one node on the list;
	head = tail = null;
	else {              // if more than one node in the list;
	head = head.next;
	head.prev = null;
	}
	return el;
	}
	public T deleteFromTail() 
	{
		if (isEmpty()) 
		return null;
		T el = tail.info;
		if (head == tail)   // if only one node on the list;
			head = tail = null;
		else 
		{              // if more than one node in the list;
			tail = tail.prev;
			tail.next = null;
		}
		return el;
	}
	public void printAll() 
	{ 
		for (DLLNode<T> tmp = head; tmp != null; tmp = tmp.next)
		System.out.print(tmp.info + " ");
	}
	public T find(T el) 
	{
		DLLNode<T> tmp;
		for (tmp = head; tmp != null && tmp.info != el; tmp = tmp.next);
		if (tmp == null)
		return null;
		else return tmp.info;
	}
	
	public T dequeue()
	{
		return deleteFromTail();
	}
	
	public int size()
	{
		if (!isEmpty())
		{
			if (head == tail)
			{
				return 1;
			}
			else
			{
				int counter = 0;
				DLLNode<T> tmp = head;
				while (tmp.next != null)
				{
					tmp = tmp.next;
					counter++;
					
				}
				return counter;
			}
		}
		else
		{
			return 0;
		}
		
	}
	
	public void enqueue(T el)
	{
		addToTail(el);
	}
	
	public boolean inQueue(T el)
	{
		if (find(el) != null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void remove(T el)
	{
		if (find(el) != null)
		{
			DLLNode<T> tmp = head;
			
			if (head.info == el)
			{
				tmp = head;
				head = head.next;
			}
			else if (tail.info == el)
			{
				tail = tail.prev;
			}
			else
			{
				while (tmp.info != el && tmp.next != null)
				{
					tmp = tmp.next;
				}
				
				if (tmp.info == el)
				{
					DLLNode<T> temp = tmp;
					
					//temp is the one we are looking at
					tmp.prev.next = tmp.next;
					temp.next.prev = tmp;
					
					
				}
			}
			
			
			
		}
	}
	public T get (int i)
	{
		//0 = head
		
		DLLNode<T> tmp = head;
		for (int x = 0; x < i; x++)
		{
			tmp = tmp.next;
		}
		
		
		return tmp.info;
	}
}
