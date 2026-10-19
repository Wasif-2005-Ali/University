INCLUDE Irvine32.inc

.data
         promptMsg  BYTE "Enter Hours: ", 0
         workingMsg BYTE "Working Hours", 0
         leisureMsg BYTE "Leisure Hours", 0
         hour       DWORD ?

.code
main PROC
             ; cout << "Enter Hours:" << endl;
             mov  edx, OFFSET promptMsg
             call WriteString

             ; cin >> hour;
             call ReadInt
             mov  hour, eax

             ; if (hour >= 9 && hour <= 18)
             cmp  hour, 9
             jl   LEISURE                   ; If hour is less than 9, jump to else block

             cmp  hour, 18
             jg   LEISURE                   ; If hour is greater than 18, jump to else block

             ; Working Hours block (Executes if 9 <= hour <= 18)
             mov  edx, OFFSET workingMsg
             call WriteString
             jmp  END_IF

    LEISURE:
             ; Leisure Hours block
             mov  edx, OFFSET leisureMsg
             call WriteString

    END_IF:
             call Crlf
             exit
main ENDP
END main