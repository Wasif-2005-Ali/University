INCLUDE Irvine32.inc

.data
    prompt1 DB "Enter first number: ", 0
    prompt2 DB "Enter second number: ", 0
            v1 SDWORD ?
            v2 SDWORD ?

.code
main PROC
                   ; Input
                   mov  edx, OFFSET prompt1
                   call WriteString
                   call ReadInt
                   mov  v1, eax

                   mov  edx, OFFSET prompt2
                   call WriteString
                   call ReadInt
                   mov  v2, eax

                   ; Call Functions
                   call Addition
                   call Subtraction
                   call Multiplication

                   exit
main ENDP

Addition PROC
                   mov  eax, v1
                   add  eax, v2
                   call WriteInt
                   call crlf
                   ret
Addition ENDP

Subtraction PROC
                   mov  eax, v1
                   sub  eax, v2
                   call WriteInt
                   call crlf
                   ret
Subtraction ENDP

Multiplication PROC
                   mov  eax, v1
                   imul eax, v2
                   call WriteInt
                   call crlf
                   ret
Multiplication ENDP

END main