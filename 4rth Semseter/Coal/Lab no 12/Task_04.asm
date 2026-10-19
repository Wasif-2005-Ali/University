INCLUDE Irvine32.inc

.data
          array   DWORD 0,0,0,0,15,20
    count =       LENGTHOF array

          msg     BYTE "First non-zero value: ",0
          noneMsg BYTE "No non-zero value found.",0

.code
main PROC

                mov  esi, OFFSET array
                mov  ecx, count

    SearchLoop:
                mov  eax, [esi]

                cmp  eax, 0
                jne  Found

                add  esi, TYPE array
                loop SearchLoop

                mov  edx, OFFSET noneMsg
                call WriteString
                jmp  Finish

    Found:
                mov  edx, OFFSET msg
                call WriteString
                call WriteDec

    Finish:
                call Crlf
                exit

main ENDP
END main