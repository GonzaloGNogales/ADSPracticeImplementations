package material.linear;

public class ArrayQueue<E> implements Queue<E> {
    int top;
    E[] array;
    int size;
    int length;
    int nextFree;

    public ArrayQueue () {
        this.top = 0;
        this.array = (E[]) new Object[16];
        this.size = 0;
        this.nextFree = 0;
        this.length = 16;
    }

    public ArrayQueue (int capacity) {
        this.top = 0;
        this.array = (E[]) new Object[capacity];
        this.size = 0;
        this.nextFree = 0;
        this.length = capacity;
    }


    @Override
    public int size () {
        return this.size;
    }

    @Override
    public boolean isEmpty () {
        return this.size == 0;
    }

    @Override
    public E front () {
        if (this.isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }

        return this.array[this.top];
    }

    @Override
    public void enqueue (E element) {
        if (this.nextFree < this.length) {
            this.array[this.nextFree] = element;
            this.size++;
            this.nextFree++;
        }
        else if (this.nextFree == this.length) {
            this.length *= 2;
            E[] arrayResized = (E[]) new Object[this.length];

            for (int i = 0; i < this.nextFree; i++) {
                arrayResized[i] = this.array[i];
            }

            this.array = arrayResized;
            this.array[this.nextFree] = element;
            this.size++;
            this.nextFree++;
        }
    }

    @Override
    public E dequeue () {
        if (this.isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }

        E value = this.array[this.top];
        this.top++;
        this.size--;

        return value;
    }
}
