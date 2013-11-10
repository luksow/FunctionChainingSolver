# FunctionChainingSolver

Simple one-file solver that given list of functions (X1 -> Y1, X2 -> Y2, ..., Xn -> Yn) and two types T and R finds a path that X1 =:= T, Yn =:= R and Yk =:= X(k + 1). To put it informally, you give functions (funs), expected input (T) and output type (R) and solver finds feasible function composition T -> R using funs. This project is probably useless and served me as a way to play with Scala's reflection.

## Examples

Go [here](src/main/scala/com/theiterators/utils/ExampleSpec.scala).

## License

See LICENSE file.

## Contact

If you have any questions, don't hesitate to contact me:

Łukasz Sowa

web: http://lukaszsowa.pl | http://www.theiterators.com

mail: lukasz [at] theiterators [dot] com | contact [at] lukaszsowa [dot] pl