INCLUDE c:\Users\Wasif Ali\.vscode\extensions\istareatscreens.masm-runner-0.9.1\native\irvine\Irvine32.inc

.data
    ; Test variables for the conditions
    op1 DWORD 10
    op2 DWORD 10
    X   DWORD 20
    Y   DWORD 15
    
    ; Messages for routines
    msg1 BYTE "Routine1 called (op1 == op2 AND X > Y)", 0
    msg2 BYTE "Routine2 called (op1 == op2 AND X <= Y)", 0
    msg3 BYTE "Routine3 called (op1 != op2)", 0

.code
main PROC
    mov eax, op1
    cmp eax, op2
    jne L3          ; If op1 != op2, jump to outer else block

    ; Inner IF condition (op1 == op2)
    mov eax, X
    cmp eax, Y
    jle L2          ; If X <= Y, jump to inner else block

    call Routine1   ; Executes if X > Y
    jmp END_IF

L2:
    call Routine2   ; Executes if X <= Y
    jmp END_IF

L3:
    call Routine3   ; Executes if op1 != op2

END_IF:
    call Crlf
    exit
main ENDP

; --- Procedures ---
Routine1 PROC
    mov edx, OFFSET msg1
    call WriteString
    ret
Routine1 ENDP

Routine2 PROC
    mov edx, OFFSET msg2
    call WriteString
    ret
Routine2 ENDP

Routine3 PROC
    mov edx, OFFSET msg3
    call WriteString
    ret
Routine3 ENDP

END main