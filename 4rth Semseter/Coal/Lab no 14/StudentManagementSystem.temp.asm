; ============================================================================
; STUDENT MANAGEMENT SYSTEM - BONUS VERSION
; With Average Marks & Pass/Fail Functionality
; Author: Wasif
; Course: CCP (Computer Architecture Programming)
; Library: Irvine32
; ============================================================================

INCLUDE c:\Users\Wasif Ali\.vscode\extensions\istareatscreens.masm-runner-0.9.1\native\irvine\Irvine32.inc

.data
         ; Array to store marks of 5 students
         marks           DWORD 5 DUP(0)

         ; Menu strings
         menuTitle       BYTE "======================================", 0dh, 0ah
         BYTE            "  STUDENT MANAGEMENT SYSTEM", 0dh, 0ah
         BYTE            "======================================", 0dh, 0ah, 0

         menuOptions     BYTE "1. Enter Student Marks", 0dh, 0ah
         BYTE            "2. Display All Marks", 0dh, 0ah
         BYTE            "3. Calculate Total Marks", 0dh, 0ah
         BYTE            "4. Find Highest and Lowest Marks", 0dh, 0ah
         BYTE            "5. Calculate Average Marks", 0dh, 0ah
         BYTE            "6. Show Pass/Fail Status", 0dh, 0ah
         BYTE            "7. Exit", 0dh, 0ah
         BYTE            "======================================", 0dh, 0ah, 0

         choicePrompt    BYTE "Enter your choice (1-7): ", 0
         invalidChoice   BYTE "Invalid choice! Please try again.", 0dh, 0ah, 0
         invalidMark     BYTE "Invalid mark! Please enter a value between 0-100.", 0dh, 0ah, 0

         ; Input strings
         enterMarkPrompt BYTE "Enter marks for 5 students (0-100):", 0dh, 0ah, 0
         studentPrompt   BYTE "Student ", 0
         colonPrompt     BYTE ": ", 0

         ; Display strings
         marksList       BYTE "======================================", 0dh, 0ah
         BYTE            "Marks List:", 0dh, 0ah, 0

         totalPrompt     BYTE "Total Marks = ", 0
         averagePrompt   BYTE "Average Marks = ", 0

         minMaxPrompt    BYTE "======================================", 0dh, 0ah
         BYTE            "Highest Marks = ", 0

         lowestPrompt    BYTE "Lowest Marks = ", 0

         ; Pass/Fail strings
         passFailHeader  BYTE "======================================", 0dh, 0ah
         BYTE            "Pass/Fail Status (Pass >= 50):", 0dh, 0ah, 0

         passMsg         BYTE " - PASS", 0dh, 0ah, 0
         failMsg         BYTE " - FAIL", 0dh, 0ah, 0

         separator       BYTE 0dh, 0ah, 0
         space           BYTE " ", 0

         ; Confirmation messages
         exitMsg         BYTE "Exiting program. Goodbye!", 0dh, 0ah, 0

.code

; ============================================================================
; MAIN PROCEDURE - Entry point and menu loop
; ============================================================================
main PROC

    menuLoop:
                       ; Display menu
                       mov  edx, OFFSET menuTitle
                       call WriteString

                       mov  edx, OFFSET menuOptions
                       call WriteString

                       ; Get user choice
                       mov  edx, OFFSET choicePrompt
                       call WriteString
                       call ReadInt

                       ; Validate choice (must be 1-7)
                       cmp  eax, 1
                       jl   invalidMenuChoice
                       cmp  eax, 7
                       jg   invalidMenuChoice

                       ; Branch to appropriate procedure
                       cmp  eax, 1
                       je   callInput
                       cmp  eax, 2
                       je   callDisplay
                       cmp  eax, 3
                       je   callTotal
                       cmp  eax, 4
                       je   callMinMax
                       cmp  eax, 5
                       je   callAverage
                       cmp  eax, 6
                       je   callPassFail
                       cmp  eax, 7
                       je   exitProgram

    callInput:
                       call InputMarks
                       jmp  menuLoop

    callDisplay:
                       call DisplayMarks
                       jmp  menuLoop

    callTotal:
                       call CalculateTotal
                       jmp  menuLoop

    callMinMax:
                       call FindMinMax
                       jmp  menuLoop

    callAverage:
                       call CalculateAverage
                       jmp  menuLoop

    callPassFail:
                       call ShowPassFail
                       jmp  menuLoop

    invalidMenuChoice:
                       mov  edx, OFFSET invalidChoice
                       call WriteString
                       jmp  menuLoop

    exitProgram:
                       mov  edx, OFFSET exitMsg
                       call WriteString
                       exit

main ENDP

; ============================================================================
; INPUTMARKS PROCEDURE - With validation for 0-100 range
; ============================================================================
InputMarks PROC
                       mov  edx, OFFSET enterMarkPrompt
                       call WriteString

                       mov  ecx, 0
                       mov  ebx, OFFSET marks

    inputLoop:
                       cmp  ecx, 5
                       jge  inputDone

                       ; Display "Student X: "
                       mov  edx, OFFSET studentPrompt
                       call WriteString

                       mov  eax, ecx
                       inc  eax
                       call WriteInt

                       mov  edx, OFFSET colonPrompt
                       call WriteString

    inputRetry:
                       call ReadInt

                       ; Validate: mark must be between 0 and 100
                       cmp  eax, 0
                       jl   invalidInput
                       cmp  eax, 100
                       jg   invalidInput

                       ; Store valid mark
                       mov  [ebx + ecx*4], eax
                       inc  ecx
                       jmp  inputLoop

    invalidInput:
                       mov  edx, OFFSET invalidMark
                       call WriteString

                       mov  edx, OFFSET studentPrompt
                       call WriteString
                       mov  eax, ecx
                       inc  eax
                       call WriteInt
                       mov  edx, OFFSET colonPrompt
                       call WriteString
                       jmp  inputRetry

    inputDone:
                       call Crlf
                       ret

InputMarks ENDP

; ============================================================================
; DISPLAYMARKS PROCEDURE
; ============================================================================
DisplayMarks PROC
                       mov  edx, OFFSET marksList
                       call WriteString

                       mov  ecx, 0
                       mov  ebx, OFFSET marks

    displayLoop:
                       cmp  ecx, 5
                       jge  displayDone

                       mov  eax, [ebx + ecx*4]
                       call WriteInt

                       mov  edx, OFFSET space
                       call WriteString

                       inc  ecx
                       jmp  displayLoop

    displayDone:
                       mov  edx, OFFSET separator
                       call WriteString
                       ret

DisplayMarks ENDP

; ============================================================================
; CALCULATETOTAL PROCEDURE
; ============================================================================
CalculateTotal PROC
                       mov  eax, 0
                       mov  ecx, 0
                       mov  ebx, OFFSET marks

    totalLoop:
                       cmp  ecx, 5
                       jge  totalDone

                       add  eax, [ebx + ecx*4]
                       inc  ecx
                       jmp  totalLoop

    totalDone:
                       mov  edx, OFFSET totalPrompt
                       call WriteString
                       call WriteInt

                       mov  edx, OFFSET separator
                       call WriteString
                       ret

CalculateTotal ENDP

; ============================================================================
; CALCULATEAVERAGE PROCEDURE
; Description: Calculate and display average of marks
; ============================================================================
CalculateAverage PROC
                       mov  eax, 0
                       mov  ecx, 0
                       mov  ebx, OFFSET marks

    ; Calculate total
    sumLoop:
                       cmp  ecx, 5
                       jge  sumDone

                       add  eax, [ebx + ecx*4]
                       inc  ecx
                       jmp  sumLoop

    sumDone:
                       ; Divide by 5 to get average
                       mov  ecx, 5
                       cdq                                 ; Convert EAX to 64-bit for division
                       idiv ecx                            ; EAX = quotient (average)

                       ; Display result
                       mov  edx, OFFSET averagePrompt
                       call WriteString
                       call WriteInt

                       mov  edx, OFFSET separator
                       call WriteString
                       ret

CalculateAverage ENDP

; ============================================================================
; FINDMINMAX PROCEDURE
; ============================================================================
FindMinMax PROC
                       mov  ebx, OFFSET marks

                       ; Initialize with first mark
                       mov  eax, [ebx + 0]
                       mov  ecx, eax                       ; Max
                       mov  ebx, eax                       ; Min

                       mov  edx, 1
                       mov  esi, OFFSET marks

    findLoop:
                       cmp  edx, 5
                       jge  findDone

                       mov  eax, [esi + edx*4]

                       cmp  eax, ecx
                       jle  checkMin
                       mov  ecx, eax

    checkMin:
                       cmp  eax, ebx
                       jge  skipMin
                       mov  ebx, eax

    skipMin:
                       inc  edx
                       jmp  findLoop

    findDone:
                       mov  edx, OFFSET minMaxPrompt
                       call WriteString
                       mov  eax, ecx
                       call WriteInt

                       mov  edx, OFFSET separator
                       call WriteString

                       mov  edx, OFFSET lowestPrompt
                       call WriteString
                       mov  eax, ebx
                       call WriteInt

                       mov  edx, OFFSET separator
                       call WriteString
                       ret

FindMinMax ENDP

; ============================================================================
; SHOWPASSFAIL PROCEDURE
; Description: Show Pass/Fail status for each student (Pass >= 50)
; ============================================================================
ShowPassFail PROC
                       mov  edx, OFFSET passFailHeader
                       call WriteString

                       mov  ecx, 0
                       mov  ebx, OFFSET marks

    passFailLoop:
                       cmp  ecx, 5
                       jge  passFailDone

                       ; Print student number
                       mov  edx, OFFSET studentPrompt
                       call WriteString

                       mov  eax, ecx
                       inc  eax
                       call WriteInt

                       ; Load mark
                       mov  eax, [ebx + ecx*4]

                       ; Check if mark >= 50 (Pass)
                       cmp  eax, 50
                       jl   failStudent

                       ; Print PASS
                       mov  edx, OFFSET passMsg
                       call WriteString
                       jmp  nextStudent

    failStudent:
                       ; Print FAIL
                       mov  edx, OFFSET failMsg
                       call WriteString

    nextStudent:
                       inc  ecx
                       jmp  passFailLoop

    passFailDone:
                       mov  edx, OFFSET separator
                       call WriteString
                       ret

ShowPassFail ENDP

END main