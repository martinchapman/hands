package Utility;

import java.util.ArrayList;

public class Node<V> {

	ArrayList<Node<V>> children;
	
	Node<V> parent;
	
	public Node<V> getParent() {
	
		return parent;
	
	}

	V value;
	
	public V getValue() {
		
		return value;
	
	}

	public Node(V value, Node<V> parent) {
		
		this.value = value;
		
		this.parent = parent;
		
		children = new ArrayList<Node<V>>();
		
	}
	
	public ArrayList<Node<V>> getChildren() {
		
		return children;
		
	}
	
	public void addChild(Node<V> child) {
		
		children.add(child);
		
	}
	
	public void printChildren() {
		
		for ( Node<V> child : children ) {
			
			System.out.print(child + " ");
			
		}
		
		System.out.println("");
		
	}
	
	public String toString() {
		
		return value + "";
		
	}
	
}
