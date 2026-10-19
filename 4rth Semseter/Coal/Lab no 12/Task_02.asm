; Author: Wasif Ali
; Task 2: Compare X and Y, with a continue prompt
INCLUDE Irvine32.inc

.data
         promptX        BYTE "Enter value for X: ", 0
         promptY        BYTE "Enter value for Y: ", 0
         msgGreater     BYTE "X is greater than Y", 0
         msgLess        BYTE "X is less than Y", 0
         msgEqual       BYTE "X is equal to Y", 0
         promptContinue BYTE "Do you want to continue? (y/n): ", 0

         valX           SDWORD ?
         valY           SDWORD ?

.code
main PROC
    program_loop:
                  ; Get X
                  mov  edx, OFFSET promptX
                  call WriteString
                  call ReadInt
                  mov  valX, eax

                  ; Get Y
                  mov  edx, OFFSET promptY
                  call WriteString
                  call ReadInt
                  mov  valY, eax

                  ; Compare X and Y
                  mov  eax, valX
                  cmp  eax, valY
                  jg   x_is_greater
                  jl   x_is_less

                  ; If neither greater nor less, they are equal
                  mov  edx, OFFSET msgEqual
                  jmp  print_result

    x_is_greater:
                  mov  edx, OFFSET msgGreater
                  jmp  print_result

    x_is_less:
                  mov  edx, OFFSET msgLess

    print_result:
                  call WriteString
                  call Crlf

                  ; Ask to continue
                  mov  edx, OFFSET promptContinue
                  call WriteString
                  call ReadChar
                  call WriteChar                     ; Echo the character back to the console
                  call Crlf
                  call Crlf

                  cmp  al, 'y'
                  je   program_loop
                  cmp  al, 'Y'
                  je   program_loop

                  exit
main ENDP
END main