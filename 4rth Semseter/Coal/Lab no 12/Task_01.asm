INCLUDE Irvine32.inc

.data
          array   SDWORD -5, -9, -3, 0, -7, 8, 10
    count =       LENGTHOF array

          msg     BYTE "First positive value found: ",0
          noneMsg BYTE "No positive value found.",0

.code
main PROC

                   mov  esi, OFFSET array
                   mov  ecx, count

    L1:
                   mov  eax, [esi]

                   test eax, eax               ; sets SF/ZF
                   jg   FoundPositive          ; jump if > 0

                   add  esi, TYPE array
                   loop L1

                   mov  edx, OFFSET noneMsg
                   call WriteString
                   jmp  ExitProgram

    FoundPositive:
                   mov  edx, OFFSET msg
                   call WriteString
                   call WriteInt

    ExitProgram:
                   call Crlf
                   exit

main ENDP
END main