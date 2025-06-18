/*package com.yandex.hw.manager.history;


import com.yandex.hw.model.Task;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    public final List<Task> history = new ArrayList<>();

    class Node<Task> {
        public Task data;
        public  Node<Task> next;
        public Node<Task> prev;
        public Node(Node<Task> prev, Task data, Node<Task> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
     }

    public Node<Task>  tail;
    public Node<Task>  head;
    public Node<Task> node;
    public final HashMap<Integer, Node<Task>> data = new HashMap<>();

    public Node<Task> linkLast(Task task){

        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(null, task, oldTail);
        tail = newNode;
        if (oldTail == null){
            head = newNode;
        }else {
            oldTail.next = newNode;
        }
        return newNode;
    }
    public void removeNode(Node<Task> node){
        if (node == null) {
            return; // или выбрасываем исключение, например: throw new IllegalArgumentException("Node cannot be null");
        }
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next; //
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
        node.next = null;
        node.prev = null;
    }
    @Override
    public void add(Task task) {
        if (data.size() > 10) {
            data.remove(0);
        }
        int id = task.getId();
        if(data.containsKey(id)){
           // data.remove(id);
            removeNode(data.get(id));
        }
      Node node =  linkLast(task);

       data.put(id, node);

     //  history.add(task);

    }

    @Override
    public ArrayList<Task> getHistory() {
        for (int i = 0 ; i < data.size(); i++) {

            Task task = data.get(i).data;
         //   System.out.println(task);
            history.add(task);
        }

        return new ArrayList<>(history);
    }

    @Override
    public void remove(int id) {
        data.remove(id);
    }

}*/
package com.yandex.hw.manager.history;

import com.yandex.hw.model.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node<Task>> data = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;
    private static final int MAX_HISTORY_SIZE = 10;

    private static class Node<Task> {
        public Task data;
        public Node<Task> next;
        public Node<Task> prev;

        public Node(Node<Task> prev, Task data, Node<Task> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    private Node<Task> linkLast(Task task) {
        final Node<Task> newNode = new Node<>(tail, task, null);
        if (tail == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
        return newNode;
    }

    private void removeNode(Node<Task> node) {
        if (node == null) return;

        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
    }

    @Override
    public void add(Task task) {
        if (task == null) return;

        int id = task.getId();

        // Если задача уже есть в истории, удаляем её старую версию
        if (data.containsKey(id)) {
            removeNode(data.get(id));
            data.remove(id);
        }

        // Если история слишком большая, удаляем самую старую задачу
        if (data.size() >= MAX_HISTORY_SIZE && head != null) {
            data.remove(head.data.getId());
            removeNode(head);
        }

        // Добавляем задачу в конец списка
        Node<Task> newNode = linkLast(task);
        data.put(id, newNode);

    }

    @Override
    public ArrayList<Task> getHistory() {
        ArrayList<Task> history = new ArrayList<>();
        Node<Task> current = head;
        while (current != null) {
            history.add(current.data);
            current = current.next;
        }
      //  System.out.println(data.size());
        return history;
    }

    @Override
    public void remove(int id) {
        if (data.containsKey(id)) {
            removeNode(data.get(id));
            data.remove(id);
        }
    }
}
