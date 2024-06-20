a. Describe briefly the functioning of the fork system call and the values it can return.
- The fork will create a new process. If the process is created successfully it will return the process id in the
parent process 0 in the child process, whereas if the creation of the child process failed, it will return -1.


b. What will print to the screen the program fragment below, considering that the fork system 
call is successful? Justify your answer.
```cpp
int main() {
    int n = 1;
    if(fork() == 0) {
        n = n + 1;
        exit(0);
    }
    n = n + 2;
    printf(“%d: %d\n”, getpid(), n);
    wait(0);
    return 0;
}
```
- It will print process id: 3

c. What will print to the screen the shell script fragment below? Explain the functioning of 
the first three lines of the fragment

```bash
for F in *.txt; do
    K=`grep abc $F`
    if [ “$K” != “” ]; then
        echo $F
    fi
done
```
- it will print the name of the files which ends in .txt in which exists the word abc

a. Consider the code fragment below. What lines will it print to the screen and in what order, 
considering that the fork system call is successful? Justify your answer.
```cpp
int main() {
    int i;
    for(i=0; i<2; i++) {
        printf("%d: %d\n", getpid(), i);
        if(fork() == 0) {
            printf("%d: %d\n", getpid(), i);
            exit(0);
        }
    }
    for(i=0; i<2; i++) {
        wait(0);
    }
    return 0;
}
```
- It should print process id: 0, process id: 1, process id: 0, process id: 1, not exactly in this order

b. Explain the functioning of the shell script fragment below. What happens if the file 
report.txt is missing initially? Add the line of code missing for generating the file report.txt
```bash
    more report.txt
    rm report.txt
    for f in *.sh; do
        if [ ! -x $f ]; then
            chmod 700 $f
        fi
    done
    mail -s "Affected files report" admin@scs.ubbcluj.ro <report.txt
```
The script would fail if the report.txt does not exist. To solve this add as first line touch report.txt.

E1. Store in file a.txt all the processes in the system with details, replacing all sequences of 
one or more spaces with a single space.
```bash
ps -e -f | sed "s/ \+/ /g" > a.txt
```
