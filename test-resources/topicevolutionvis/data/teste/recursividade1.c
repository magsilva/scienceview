
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
int inverteNumero(int num){
     return num < 10 ? num:(num % 10) * pow(10, (int)log10(num)) + inverteNumero(num/10);
}

// Escreva uma função recursiva que receba como parâmetro um número inteiro N (considere que N é
// positivo e não nulo) e imprima na tela os números de de 0 até N em ordem crescente.
void imprimeCrescente(int num){
     if (num < 0) {
          return;
     }
     imprimeCrescente(num-1);
     printf("%d ", num);
}

// Escreva uma função recursiva que receba como parâmetro um número inteiro N (considere que N é
// positivo e não nulo) e imprima na tela os números de N até 0 em ordem decrescente
void imprimeDecrescente(int num){
     if (num < 0) {
          return;
     }
     printf("%d ", num);
     imprimeDecrescente(num-1);
}

// Escreva uma função recursiva em C que retorna 1 se um número dado como parâmetro é primo, ou 0
// caso não seja. O nome da função deve ser isPrimo.
int isPrimo(int num, int count){
     if (num == 1) {
          return 0;
     }
     if (count > sqrt(num)){
          return 1;
     }
     if ((num % count) == 0) {
          return 0;
     }
     return isPrimo(num, count+1);
}

// Considere a função Comb(n, k), que representa o número de grupos distintos com k pessoas que podem
// ser formados a partir de n pessoas. Por exemplo, Comb(4, 3) = 4, pois com 4 pessoas (A, B, C, D), é
// possível formar 4 diferentes grupos: ABC, ABD, ACD e BCD.
// Implemente uma função recursiva para Comb (n, k)
int combinatoria(int num1, int num2){
     if (num1 == num2) {
          return 1;
     }
     if (num2 == 1) {
          return num1;
     }
     return combinatoria(num1-1, num2-1) + combinatoria(num1-1, num2);
}


// A Multiplicação Russa é uma técnica de multiplicar 2 números baseada em um princípio de
// multiplicação por duplicação. Embora essa técnica não é tão rápida quando a multiplicação
// convencional, podemos implementa-la.
int multiplicacaoRussa(int num1, int num2){
     if ((num1) == 0) {
          return 0;
     }
     return ((num1 % 2) != 0) ? num2 + multiplicacaoRussa((num1/2), (num2*2)):0 + multiplicacaoRussa((num1/2), (num2*2));
}

int main(int argc, char const *argv[]) {

     printf("\n\n");
     printf("Lista de Recursividade");
     printf("\n\n");

     int exe1 = inverteNumero(123);
     printf("Exercicio_1:\n%d\n", exe1);
     printf("\n");

     printf("Exercicio_2:\n");
     imprimeCrescente(10);
     printf("\n");
     printf("\n");

     printf("Exercicio_3:\n");
     imprimeDecrescente(10);
     printf("\n");
     printf("\n");

     int exe4 = isPrimo(10, 2);
     printf("Exercicio_4:\n%d\n", exe4);
     printf("\n");
     printf("\n");

     int exe5 = combinatoria(16, 4);
     printf("Exercicio_5:\n%d\n", exe5);
     printf("\n");
     printf("\n");

     int exe6 = multiplicacaoRussa(10, 10);
     printf("Exercicio_6:\n%d\n", exe6);
     printf("\n");
     printf("\n");



     return 0;
}
