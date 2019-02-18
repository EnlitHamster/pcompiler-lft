# Usage
To compile a single exercise, use `./es.sh <paragraph> <exercise> [force build]`  
To compile a single file (with due dependencies) use `./exec.sh build <file name> [force build]`
To test a project use `./exec.sh test <project>`
To compile the entire project use `make`
To clear the executables use `make clear`

# Context-Free Grammar of P

```
P   ->   SL $

SL  ->   S SL'

SL' ->   ; S SL'
     |   ε

S   ->   id := E
     |   print ( E )
     |   read ( id )
     |   case WL else S
     |   while ( BO ) S
     |   { SL }
     
WL  ->   W WL'

WL' ->   W WL'
     |   ε

W   ->   when ( BO ) S

BO  ->   BA BO'

BO' ->   or BA BO'
     |   ε

BA  ->   B BA'

BA' ->   and B BA'
     |   ε

B   ->   E RELOP E
     |   ! ( E RELOP E )
     
E   ->   T E'

E'  ->   + T E'
     |   - T E'
     |   ε
     
T   ->   F T'

T'  ->   * F T'
     |   / F T'
     |   ε

F   ->   ( E )
     |   num
     |   id
```
