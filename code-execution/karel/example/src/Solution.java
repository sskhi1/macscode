public class Solution extends Karel {

    public void run() {
        putBeeper();
        walkToWall();
        putBeeper();
        turnAround();
        walkToBeeper();
        while (noBeepersPresent()){
            putBeeper();
            turnAround();
            walkToBeeper();
        }
        clearRightOfCenter();
        clearLeftOfCenter();
        turnAround();
        walkToBeeper();
        move();

    }

    /* METHOD: clearRightOfCenter()
     *
     * This will clear all Beepers to the right of the Center.
     *
     * pre-condition:
     * Karel is in the middle of the grid standing on a beeper,
     * facing East.
     *
     * post-condition:
     * Karel is standing at the far-right of the first row, having
     * cleared all the beepers from the center along the way.
     */

    private void clearRightOfCenter(){
        clearToWall();
    }

    /* METHOD: clearRightOfCenter()
     *
     * This will clear all Beepers to the wall in whatever way
     * Karel is facing. There can only be one or zero beepers on
     * each square.
     *
     * pre-condition:
     * Karel is facing in any direction.
     *
     * post-condition:
     * Karel has now travelled to the wall and has cleared all
     * beepers along the way.
     */

    private void clearToWall(){
        while (frontIsClear()){
            move();
            if (beepersPresent()){
                pickBeeper();
            }
        }
    }

    /* METHOD: clearLeftOfCenter()
     *
     * Karel will turn around, walk to the center beeper, than
     * walk to the left wall clearing all the beepes along
     * the way.
     *
     * pre-condition:
     * Karel is at the far right corner of bottom row facing East
     * with no beepers between Karel and the center beeper.
     *
     * post-condition:
     * Karel is standing at the far-left corner of the first row,
     * facing west, having cleared all the beepers from the center
     * to the left wall. There is no only one beeper in the center
     * of the bottom row.
     */

    private void clearLeftOfCenter(){
        turnAround();
        walkToBeeper();
        move();
        while (frontIsClear()){
            move();
            pickBeeper();
        }
    }

    /* METHOD: walkToWall()
     *
     * Facing in any direction, Karel will walk until he hits a
     * wall.
     */

    private void walkToWall(){
        while (frontIsClear()){
            move();
        }
    }

    /* METHOD: walkToBeeper()
     *
     * Facing in any direction, Karel will walk until there is a
     * beeper directly in front of him.
     */

    private void walkToBeeper(){
        move();
        while (noBeepersPresent()){
            move();
        }
        moveBackward();
    }

    /* METHOD: moveBackware()
     *
     * Karel will move backward one square while remaining
     * facing in the same direction.
     */

    private void moveBackward(){
        turnAround();
        move();
        turnAround();
    }
}