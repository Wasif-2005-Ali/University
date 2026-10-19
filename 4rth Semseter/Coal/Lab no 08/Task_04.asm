INCLUDE Irvine32.inc

.code
main PROC

         ; -------------------------------
         ; Example 1: Set Overflow (OF = 1)
         ; -------------------------------
         mov  al, 7Fh     ; 7Fh = +127 (max positive for signed 8-bit)
         add  al, 1       ; 127 + 1 = -128 (overflow in signed)
         ; OF = 1 because result exceeds signed range
         call DumpRegs

         ; -------------------------------
         ; Example 2: Clear Overflow (OF = 0)
         ; -------------------------------
         mov  al, 10h     ; small positive number
         add  al, 10h     ; normal addition (no overflow)
         ; OF = 0
         call DumpRegs

         ; -------------------------------
         ; Example 3: Subtraction causing Overflow
         ; -------------------------------
         mov  al, 80h     ; 80h = -128 (min signed)
         sub  al, 1       ; -128 - 1 = +127 (overflow)
         ; OF = 1 (out of signed range)
         call DumpRegs

         ; -------------------------------
         ; Example 4: ADD sets both CF and OF
         ; -------------------------------
         mov  al, 7Fh     ; +127
         add  al, 81h     ; +127 + (-127) but unsigned overflow occurs
         ; Result causes:
         ; CF = 1 (unsigned overflow)
         ; OF = 1 (signed overflow)
         call DumpRegs

         exit

main ENDP
END main