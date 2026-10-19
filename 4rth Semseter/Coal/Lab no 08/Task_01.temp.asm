INCLUDE c:\Users\Wasif Ali\.vscode\extensions\istareatscreens.masm-runner-0.9.1\native\irvine\Irvine32.inc

.data
Val1 BYTE 25h
Val2 BYTE 70h

.code
mov AL,Val1
xchg Val1,Val2
mov Val2,AL

main ENDP
END main