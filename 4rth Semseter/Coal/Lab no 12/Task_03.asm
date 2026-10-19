INCLUDE Irvine32.inc

.data
         num1     DWORD 0FFFFFFFFh                                                        ; A large 32-bit number
         num2     DWORD 2                                                                 ; Multiplying by 2 will push data into the upper half (EDX)
         msgSig   BYTE "The upper half of the product contains significant digits.", 0
         msgNoSig BYTE "The upper half does NOT contain significant digits.", 0

.code
main PROC
                     mov  eax, num1
                     mul  num2                    ; EDX:EAX = EAX * num2

                     jc   has_significant         ; Jump if Carry Flag is set (EDX is not zero)

                     ; If no carry, upper half is insignificant
                     mov  edx, OFFSET msgNoSig
                     call WriteString
                     call Crlf
                     jmp  quit

    has_significant:
                     mov  edx, OFFSET msgSig
                     call WriteString
                     call Crlf

    quit:
                     exit
main ENDP
END main