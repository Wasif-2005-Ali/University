INCLUDE Irvine32.inc



.code
main PROC

         ;Addition (positive result)
         ; EAX = 3 + 1 = 4
         ; ZF = 0 (result is not zero)
         ; SF = 0 (result is positive)
         ; -------------------------------
         mov  eax, 3
         add  eax, 1
         call DumpRegs    ; Display registers and flags

         ; -------------------------------
         ; CASE 2: Subtraction resulting in ZERO
         ; EAX = 4 - 4 = 0
         ; ZF = 1 (result is zero → flag set)
         ; SF = 0 (zero is not negative)
         ; -------------------------------
         sub  eax, 4
         call DumpRegs    ; ZF becomes 1 here


         ; -------------------------------
         ; CASE 3: Subtraction resulting in NEGATIVE value
         ; EAX = 0 - 5 = -5
         ; ZF = 0 (result is not zero)
         ; SF = 1 (negative result → sign bit set)
         ; -------------------------------
         sub  eax, 5
         call DumpRegs    ; SF becomes 1 here

         ; -------------------------------
         ; CASE 4: Addition producing negative result (via overflowed signed view)
         ; EAX = -5 + (-3) = -8
         ; ZF = 0 (not zero)
         ; SF = 1 (still negative)
         ; -------------------------------
         sub  eax, 3
         call DumpRegs

         exit
main ENDP

END main